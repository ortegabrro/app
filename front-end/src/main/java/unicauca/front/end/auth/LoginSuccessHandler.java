package unicauca.front.end.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		String targetUrl = determineTargetUrl(authentication);

		if (response.isCommitted()) {
			System.out.println("Can't redirect");
			return;
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	/*
	 * This method extracts the roles of currently logged-in user and returns
	 * appropriate URL according to his/her role.
	 */
	protected String determineTargetUrl(Authentication authentication) {
		String url = "";
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<String> roles = new ArrayList<String>();

		for (GrantedAuthority a : authorities) {
			roles.add(a.getAuthority());
		}

		if (isApp(roles)) {
			url = "/admin/app";
		} else if (isAdmin(roles)) {
			url = "/admin/autoridad/main";
		} else if (isJudicial(roles)) {
			url = "autoridad/judicial/gestor";
		} else if (isCoactivo(roles)) {
			url = "autoridad/coactivo/gestor";
		} else if (isAuxJudicial(roles)) {
			url = "autoridad/judicial/secretario";
		} else if (isAuxCoactivo(roles)) {
			url = "autoridad/coactivo/secretario";
		} else if (isPersona(roles)) {
			url = "/persona";
		} else if (isPersona(roles)) {
			url = "/persona";
		} else {
			url = "/accessDenied";
		}

		return url;
	}

	private boolean isApp(List<String> roles) {
		if (roles.contains("ROLE_APP")) {
			return true;
		}
		return false;
	}

	private boolean isAdmin(List<String> roles) {
		if (roles.contains("ROLE_ADMIN")) {
			return true;
		}
		return false;
	}

	private boolean isJudicial(List<String> roles) {
		if (roles.contains("ROLE_GESTOR") && roles.contains("ROLE_JUDICIAL")) {
			return true;
		}
		return false;
	}

	private boolean isCoactivo(List<String> roles) {
		if (roles.contains("ROLE_GESTOR") && roles.contains("ROLE_COACTIVO")) {
			return true;
		}
		return false;
	}

	private boolean isAuxJudicial(List<String> roles) {
		if (roles.contains("ROLE_SECRETARIO") && roles.contains("ROLE_JUDICIAL")) {
			return true;
		}
		return false;
	}

	private boolean isAuxCoactivo(List<String> roles) {
		if (roles.contains("ROLE_SECRETARIO") && roles.contains("ROLE_COACTIVO")) {
			return true;
		}
		return false;
	}

	private boolean isPersona(List<String> roles) {
		if (roles.contains("ROLE_PERSONA")) {
			return true;
		}
		return false;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

}
