package com.passhelm.passhelm.records;

public record ResetPasswordRequest(String oldPassword, String newPassword) {}
