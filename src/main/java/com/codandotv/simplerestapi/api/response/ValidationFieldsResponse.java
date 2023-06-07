package com.codandotv.simplerestapi.api.response;

import java.util.List;

public record ValidationFieldsResponse(Integer errorCode, String status, List<String> errors) {
}
