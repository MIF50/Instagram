package com.mif50.instagram.utils.common

import com.mif50.instagram.R
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasSize
import org.junit.Test

class ValidatorTest {


    @Test
    fun validator_validUsernameAndPassword_returnSuccess() {
        val email = "test@gmail.com"
        val password = "password"
        val resultValidations = Validator.validateLoginFields(email, password)
        assertThat(resultValidations, hasSize(2))
        assertThat(
            resultValidations,
            contains(
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }

    @Test
    fun validator_invalidEmailAndValidPassword_returnEmailError(){
        val email = "test"
        val password = "password"
        val resultValidations = Validator.validateLoginFields(email,password)
        assertThat(resultValidations, hasSize(2))
        assertThat(
            resultValidations,
            contains(
                Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )
    }

    @Test
    fun validator_validEmailAndInvalidPassword_returnPasswordError(){
        val email = "test@gmail.com"
        val password = "12"
        val resultValidations = Validator.validateLoginFields(email,password)
        assertThat(resultValidations, hasSize(2))
        assertThat(
            resultValidations,
            contains(
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_small_length))
            )
        )

    }

}