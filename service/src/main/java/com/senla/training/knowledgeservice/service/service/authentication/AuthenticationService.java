package com.senla.training.knowledgeservice.service.service.authentication;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface AuthenticationService {

    void authenticateUser(@Nullable String userLogin, @Nonnull String userPassword);
}
