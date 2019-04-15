package com.fhzm.common.Jwt;

import com.fhzm.common.StringUtil;
import com.fhzm.entity.generator.AuthMember;
import com.google.gson.Gson;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtils {
    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        String stringKey = JwtConstant.JWT_SECRET;

        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(stringKey);

        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

        return key;
    }

    /**
     * 创建jwt
     * @param id
     * @param authMember
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, AuthMember authMember, int ttlMillis) {
        String subject = new Gson().toJson(authMember);
        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", authMember.getUid());
        claims.put("username", authMember.getUsername());
        claims.put("phone", authMember.getPhone());
        claims.put("email", authMember.getEmail());
        claims.put("nickname", authMember.getNickname());
        String issuer =  authMember.getNickname();

        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey key = generalKey();
        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(id)                  // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           // iat: jwt的签发时间
                .setIssuer(issuer)          // issuer：jwt签发人
                .setSubject(subject)        // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key); // 设置签名使用的签名算法和签名使用的秘钥

        // 设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 创建jwt
     * @param id
     * @param issuer
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String issuer, String subject, long ttlMillis) throws Exception {

        // 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // 一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey key = generalKey();

        // 创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", "123456");
        claims.put("user_name", "admin");
        claims.put("nick_name", "X-rapido");

        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(id)                  // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           // iat: jwt的签发时间
                .setIssuer(issuer)          // issuer：jwt签发人
                .setSubject(subject)        // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key); // 设置签名使用的签名算法和签名使用的秘钥

        // 设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static JwtResult parseJWT(String jwt) throws Exception {
        JwtResult result = new JwtResult();
        try {
        	SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        	Claims claims = Jwts.parser()  //得到DefaultJwtParser
                    .setSigningKey(key)                 //设置签名的秘钥
                    .parseClaimsJws(jwt).getBody();     //设置需要解析的jwt
            result.setSuccess(true);
            result.setClaims(claims);
            result.setCode("0");
        } catch (ExpiredJwtException e) {
            result.setSuccess(true);
            result.setClaims(null);
            result.setCode("1");
            log.info("jwt超时啦");
        	System.out.println("超时啦");
        } catch (SignatureException e) {
            result.setSuccess(true);
            result.setClaims(null);
            result.setCode("2");
            log.info("jwt解析报错啦");
        } catch (Exception e) {
            result.setSuccess(true);
            result.setClaims(null);
            result.setCode("2");
            log.info("jwt解析报错啦");
        }
        return result;
    }

    /**
     * 解密jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static String getUserStrByJWT(String jwt) throws Exception {
        try {
            SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
            Claims claims = Jwts.parser()  //得到DefaultJwtParser
                    .setSigningKey(key)                 //设置签名的秘钥
                    .parseClaimsJws(jwt).getBody();     //设置需要解析的jwt
            if(claims != null && org.apache.commons.lang.StringUtils.isNotEmpty(claims.getSubject())) {
               return claims.getSubject();
            }
        } catch (ExpiredJwtException e) {
            log.info("jwt超时啦");
        } catch (SignatureException e) {
            log.info("jwt解析报错啦");
        } catch (Exception e) {
            log.info("jwt解析报错啦");
        }
        return null;
    }

    public static void main(String[] args) {
        AuthMember user = new AuthMember();
        String subject = new Gson().toJson(user);
        try {
            //String jwt = util.createJWT(Constant.JWT_ID, "Anson", subject, Constant.JWT_TTL);
        	String jwt = createJWT(JwtConstant.JWT_ID, "Anson", subject,10000);
        	System.out.println("JWT：" + jwt);
//        	Thread.sleep(15000);
            System.out.println("\n解密\n");
            JwtResult result = parseJWT(jwt);
            Claims c = result.getClaims();
            if(c != null ) {
            	 System.out.println(c.getId());
                 System.out.println(c.getIssuedAt());
                 System.out.println(c.getSubject());
                 System.out.println(c.getIssuer());
                 System.out.println(c.get("uid", String.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
