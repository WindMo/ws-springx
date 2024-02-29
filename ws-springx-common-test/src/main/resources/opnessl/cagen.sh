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


function main() {

    ### serverca
    readonly serverca="ws-serverca"
    readonly servercaKeyPwd="ws-servercaKeyPwd"
    readonly serverDname="/CN=WindShadow-server/OU=WindShadow/O=WS/L=Beijing/S=Beijing/C=CN"
    rootCAIssue ${serverca} ${servercaKeyPwd} ${serverDname} ${globalValidity}


    readonly clientca="ws-client"
    readonly clientcaKeyPwd="ws-clientKeyPwd"
    readonly clientDname="/CN=WindShadow-client/OU=WindShadow/O=WS/L=Beijing/S=Beijing/C=CN"
    rootCAIssue ${clientca} ${clientcaKeyPwd} ${clientDname} ${globalValidity}

    ### 高版本jdk的keytool的p12密钥库在低版本JDK使用时不兼容
}
set -x
main