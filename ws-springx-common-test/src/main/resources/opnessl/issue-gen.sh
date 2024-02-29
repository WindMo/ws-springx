source ./support.sh

function rootCAIssue() {

    certName=${1}
    keyPwd=${2}
    dname=${3}
    validity=${4}

    openssl genrsa -out ${certPath}/${certName}.key 2048
    openssl pkcs8 -topk8 -in ${certPath}/${certName}.key -out ${certPath}/${certName}.key.e -passout pass:${keyPwd} \
    && rm -f ${certPath}/${certName}.key \
    && mv ${certPath}/${certName}.key.e ${certPath}/${certName}.key
    openssl req -new -key ${certPath}/${certName}.key -passin pass:${keyPwd} \
    -out ${certPath}/${certName}.csr \
    -days ${validity} \
    -subj ${dname} \
    && openssl x509 -req \
    -in ${certPath}/${certName}.csr \
    -CAkey ${caPath}/${rootca}.key -passin pass:${rootcaKeyPwd} \
    -CA ${caPath}/${rootca}.crt -CAcreateserial \
    -out ${certPath}/${certName}.crt \
    -days ${validity}  \
    -extfile ${conf} \
    -extensions issue \
    && rm -f ${certPath}/${certName}.csr

    # 生成p12格式
    if [ $? -ne 0 ]; then
      return 1;
    fi
    CrtToP12 ${certPath}/${certName}.crt ${certPath}/${certName}.key ${keyPwd} ${certPath}/${certName}.p12 ${certName}
}

function main() {

    ### serverCert
    readonly serverCert="ws-serverCert"
    readonly serverCertKeyPwd="ws-serverCertKeyPwd"
    readonly serverDname="/CN=WindShadow-serverCert/OU=WindShadow/O=WS/L=Beijing/S=Beijing/C=CN"
    rootCAIssue ${serverCert} ${serverCertKeyPwd} ${serverDname} ${globalValidity}

    readonly clientCert="ws-clientCert"
    readonly clientCertKeyPwd="ws-clientCertKeyPwd"
    readonly clientDname="/CN=WindShadow-clientCert/OU=WindShadow/O=WS/L=Beijing/S=Beijing/C=CN"
    rootCAIssue ${clientCert} ${clientCertKeyPwd} ${clientDname} ${globalValidity}

    ### 高版本jdk的keytool的p12密钥库在低版本JDK使用时不兼容
}

set -x
main