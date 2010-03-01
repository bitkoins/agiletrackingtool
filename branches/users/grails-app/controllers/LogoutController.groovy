class LogoutController {

	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index = {
		session.invalidate()
		redirect(controller:'login')
	}
}
