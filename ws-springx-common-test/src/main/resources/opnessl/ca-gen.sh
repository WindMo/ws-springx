source ./support.sh


function genRootCA() {

### openssl genrsa -aes256 -passout pass:${keyPwd} -out ${certPath}/${caName}.key 2048 # 加密为pkcs1的格式
### pkcs1 转 pkcs8 要先解密再加密。即指定 -passin 和 -passout


    # 无加密
    openssl genrsa -out ${caPath}/${rootca}.key 2048
    # 加密为 pkcs8
    openssl pkcs8 -topk8 -in ${caPath}/${rootca}.key -out ${caPath}/${rootca}.key.e -passout pass:${rootcaKeyPwd} \
    && rm -f ${caPath}/${rootca}.key \
    && mv ${caPath}/${rootca}.key.e ${caPath}/${rootca}.key \
    && openssl req -new -key ${caPath}/${rootca}.key -passin pass:${rootcaKeyPwd} \
    -out ${caPath}/${rootca}.csr \
    -days ${rootcaValidity} \
    -subj ${rootDname} \
    && openssl x509 -req \
    -in ${caPath}/${rootca}.csr \
    -signkey ${caPath}/${rootca}.key -passin pass:${rootcaKeyPwd} \
    -out ${caPath}/${rootca}.crt \
    -days ${rootcaValidity} \
    -extfile ${conf} \
    -extensions ca

    # 生成cer格式
    if [ $? -ne 0 ]; then
      return 1;
    fi
    CrtToCer ${caPath}/${rootca}.crt ${caPath}/${rootca}.cer \
    && rm -f ${caPath}/${rootca}.csr

    # 导入rootCa根证书到信任库中
    if [ $? -ne 0 ]; then
          return 1;
    fi
    add2Trust ${rootca} ${caPath}/${rootca}.cer \
    && cp ${caPath}/${rootca}.crt ${trustPath}/${trustKeystore}.crt
}

set -x
clean
genRootCA