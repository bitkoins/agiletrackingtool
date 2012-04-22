security {
	// see DefaultSecurityConfig.groovy for all settable/overridable properties
	active = true 
	cacheUsers = false
	loginUserDomainClass = 'org.agiletracking.User'
	authorityDomainClass = 'org.agiletracking.Role'

  	useRequestMapDomainClass = true
	requestMapClass='org.agiletracking.Requestmap'
	    
	defaultTargetUrl="/project/list" 
	alwaysUseDefaultTargetUrl = true
	
    requestMapString = '''\
           CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON 
			  PATTERN_TYPE_APACHE_ANT 
			  /login/auth=IS_AUTHENTICATED_ANONYMOUSLY 
			  /captcha/**=IS_AUTHENTICATED_ANONYMOUSLY 
			  /register/**=IS_AUTHENTICATED_ANONYMOUSLY 
			  /js/**=IS_AUTHENTICATED_ANONYMOUSLY 
			  /css/**=IS_AUTHENTICATED_ANONYMOUSLY 
			  /images/**=IS_AUTHENTICATED_ANONYMOUSLY 
			  /plugins/**=IS_AUTHENTICATED_ANONYMOUSLY 
			  /captcha/**=IS_AUTHENTICATED_ANONYMOUSLY 
			  /register/**=IS_AUTHENTICATED_ANONYMOUSLY 
                          /**=IS_AUTHENTICATED_REMEMBERED
			  '''
}
 