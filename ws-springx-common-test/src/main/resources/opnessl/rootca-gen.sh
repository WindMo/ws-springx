### rootca
readonly caPath="./gen/ca"
readonly certPath="./gen/cert"
readonly trustPath="./gen/trust"
readonly rootca="ws-rootca"
readonly rootcaKeyPwd="rootcaKeyPwd"
readonly rootDname="/CN=WindShadow-root/OU=WindShadow/O=WS/L=Beijing/S=Beijing/C=CN"
readonly opensslCnf="openssl.cnf"
readonly rootcaValidity=36500
readonly globalValidity=3650

readonly trustKeystore="ws-trust"
readonly trustKeystorePwd="ws-trustKeystorePwd"

function clean() {

    rm -f *.p12
    rm -f *.jks
    rm -f *.cer
    rm -f *.csr
    rm -f *.crt
    rm -f *.key
    rm -f *.pem
    rm -f *.crl
    rm -f *.pass
    rm -rf ./demoCA
    rm -rf ./gen
    mkdir -p ${caPath}
    mkdir -p ${certPath}
    mkdir -p ${trustPath}
}

function CrtToP12() {

    crtFile=${1}
    keyFile=${2}
    keyPwd=${3}
    keystore=${4}
    keystoreAlias=${5}

    openssl pkcs12 -export -clcerts \
    -in ${crtFile} \
    -inkey ${keyFile} -passin pass:${keyPwd} \
    -out ${keystore} -name ${keystoreAlias} -password pass:${keyPwd}
}

function CrtToCer() {

    crtFile=${1}
    cerFile=${2}
    openssl x509 -in ${crtFile} -out ${cerFile} -outform der
}

function add2Trust() {

    caName=${1}
    caFile=${2}
    keytool -importcert -trustcacerts -deststoretype pkcs12 -alias ${caName} -keystore ${trustPath}/${trustKeystore}.p12 -storepass ${trustKeystorePwd} -file ${caFile} -noprompt
}

function genRootCA() {

### openssl genrsa -aes256 -passout pass:${keyPwd} -out ${certPath}/${caName}.key 2048 # 加密为pkcs1的格式
### pkcs1 转 pkcs8 要先解密再加密。即指定 -passin 和 -passout

    # 无加密
    openssl genrsa -out ${caPath}/${rootca}.key 2048
    # 加密为 pkcs8
    openssl pkcs8 -topk8 -in ${caPath}/${rootca}.key -out ${caPath}/${rootca}.key.e -passout pass:${rootcaKeyPwd} \
    && rm -f ${caPath}/${rootca}.key \
    && mv ${caPath}/${rootca}.key.e ${caPath}/${rootca}.key
    openssl req -new -key ${caPath}/${rootca}.key -passin pass:${rootcaKeyPwd} \
    -out ${caPath}/${rootca}.csr \
    -days ${rootcaValidity} \
    -subj ${rootDname}

    openssl x509 -req \
    -in ${caPath}/${rootca}.csr \
    -signkey ${caPath}/${rootca}.key -passin pass:${rootcaKeyPwd} \
    -extfile ${opensslCnf} -extensions v3_ca \
    -out ${caPath}/${rootca}.crt \
    -days ${rootcaValidity}

    CrtToCer ${caPath}/${rootca}.crt ${caPath}/${rootca}.cer
    rm -f ${caPath}/${rootca}.csr

    # 导入rootCa根证书到信任库中
    add2Trust ${rootca} ${caPath}/${rootca}.cer
    cp ${caPath}/${rootca}.crt ${trustPath}/${trustKeystore}.crt

}

function rootCAIssue() {

    caName=${1}
    keyPwd=${2}
    dname=${3}
    validity=${4}

    openssl genrsa -out ${certPath}/${caName}.key 2048
    openssl pkcs8 -topk8 -in ${certPath}/${caName}.key -out ${certPath}/${caName}.key.e -passout pass:${keyPwd} \
    && rm -f ${certPath}/${caName}.key \
    && mv ${certPath}/${caName}.key.e ${certPath}/${caName}.key
    openssl req -new -key ${certPath}/${caName}.key -passin pass:${keyPwd} \
    -out ${certPath}/${caName}.csr \
    -days ${validity} \
    -subj ${dname}

    openssl x509 -req \
    -in ${certPath}/${caName}.csr \
    -CAkey ${caPath}/${rootca}.key -passin pass:${rootcaKeyPwd} \
    -CA ${caPath}/${rootca}.crt -CAcreateserial \
    -extfile ${opensslCnf} -extensions v3_ca \
    -out ${certPath}/${caName}.crt \
    -days ${validity}

    rm -f ${certPath}/${caName}.csr

    CrtToP12 ${certPath}/${caName}.crt ${certPath}/${caName}.key ${keyPwd} ${certPath}/${caName}.p12 ${caName}
}

set -x
clean
genRootCA