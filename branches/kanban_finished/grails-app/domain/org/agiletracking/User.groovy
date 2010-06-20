package org.agiletracking

import org.agiletracking.Role

/**
 * User domain class.
 */
class User {
	static transients = ['pass']
	static hasMany = [authorities: Role]
	static belongsTo = Role

	/** Username */
	String username
	/** User Real Name*/
	String userRealName
	/** MD5 Password */
	String passwd
	/** enabled */
	boolean enabled

	String email
	boolean emailShow
	boolean agreeToTermsOfUse

	/** description */
	String description = ''

	/** plain password to create a MD5 password */
	String pass = '[secret]'

	static constraints = {
		username(blank: false, unique: true)
		userRealName(blank: false)
		passwd(blank: false)
		enabled()
		email(email:true,blank:false)
		agreeToTermsOfUse(inList:[true])
	}
}
