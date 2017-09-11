package de.syslord.microservices.webhooks.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonAutoDetect(
		fieldVisibility = Visibility.NONE,
		getterVisibility = Visibility.NONE,
		isGetterVisibility = Visibility.NONE)
public @interface JsonNoGetterAutodetect {

}
