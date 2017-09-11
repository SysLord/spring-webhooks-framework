# WebHooks Framework for Spring

### WebHook (WebHookDto)

Defines these fields:

 * name: user defined hook identifier
 * event: identification string of the event. Can be acquiered with Events::getExamples
 * remoteAddress: The user defined endpoint to be called in case of a fired event
 * username: optional authentication for the user defined endpoint
 * password: optional authentication for the user defined endpoint
 * postBody: optional body to be POSTed to the user defined endpoint
 * owner (internal, is the authenticated user)

### Event (WebHookEvent)
 
 * string identifier
 * optional parameters/placeholders
 * optional POST body

POST is used when the event or the user defined webhook define a body. GET otherwise. The webhook body overrides the event body. 

### Parameters/Placeholders

An Event may have parameters defined, which are populated at event creation. These can be seen with Events::getExamples. The user can user these parameters in remoteAddress or postBody.

Replacing is done by simple string replacement. The parameters need to be as such that no accidental replacements may happen.   

##### Example

 * SomethingHappendEvent.create("ThisHappened") contains parameter "{whatHappened}"
 * remoteAddress=http://example.com/restcall?foreignEndPointParam={whatHappened}
 * When the event is fired  the following url is called http://example.com/restcall?foreignEndPointParam=ThisHappened
 * The parameters can be used in remoteAddress.
 * The parameters can be used in postBody.

### Usage Scenario

The implemented service is expected to use user authentication.
The endpoints to register webhooks is not part of this framework.
The database repositories and entities are not part of this.

 1. service provider defines events: Events.addEventType(...)
 2. service provider defines users and access rights. The webhook user needs ROLE_SUBSCRIBER and EVENT_eventName
 3. service user asks for the available events and gets only the accessible events: Events::getExamples()
 4. service user creates a webhook with SecuredWebHookStorage::add(...)
 5. service user asks for his webhooks by SecuredWebHookStorage::getWebHooksForUser(...) and sees all his webHooks.
 6. When Events::fireEvent is called alls webhooks for the given fired event in SecuredWebHookStorage are given to AsyncServiceCaller.
 7. the implementation of AsyncServiceCaller is expected to execute the given ServiceCall.

### Default Setup


A queue for all fired events is created. The queue is executed asynchronously. The service calls are made with RestTemplate and Hystrix-javanica.

Requirementes:
 * needs Hystrix-javanica
 * RestTemplate Bean
 * @Import({ WebHooksConfig.class })
 * @Import({ WebHooksDefaultServiceCallerConfig.class })
 * @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) when used with SecuredWebHookStorage.
 * SecuredWebHookStorage Bean needs to be implemented 

### Custom Setup

Each service call which is created from fired events is given to an Instance of AsyncServiceCaller, which needs to be implemented.

Requirements:
 * @Import({ WebHooksConfig.class })
 * @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) if SecuredWebHookStorage is used
 * bean implementation of SecuredWebHookStorage
 * Implementation of AsyncServiceCaller. This should be asynchronous and return immediately. 

# Drawbacks

 * TBD 

 