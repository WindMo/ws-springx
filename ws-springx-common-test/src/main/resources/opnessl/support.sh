### rootca
readonly caPath="./gen/ca"
readonly certPath="./gen/cert"
readonly trustPath="./gen/trust"
readonly rootca="ws-rootca"
readonly rootcaKeyPwd="rootcaKeyPwd"
readonly rootDname="/CN=WindShadow-root/OU=WindShadow/O=WS/L=Beijing/S=Beijing/C=CN"
readonly conf="custom-conf.ini"
readonly caExt="ca-ext.ini"
readonly issueExt="issue-ext.ini"
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