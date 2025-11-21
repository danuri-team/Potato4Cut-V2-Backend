package com.potato.cut4.common.integration.dto;

public record ApplePublicKey(
    String kty,
    String kid,
    String use,
    String alg,
    String n,
    String e
) {

}
