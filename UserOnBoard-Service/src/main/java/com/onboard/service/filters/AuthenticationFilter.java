/**
 * 
 */
package com.onboard.service.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.onboard.service.model.Response;
import com.onboard.service.service.UserDetailsServices;
import com.onboard.service.util.JwtTokenUtil;



/**
 * @author Anish Singh(anish2u2@gmail.com)
 *
 * 10-Aug-2020
 */

@Component
public class AuthenticationFilter extends OncePerRequestFilter{


	private static final String TOKEN_HEADER = "x-user-onboard-token";

	@Value("${sso.auth.url}")
	private String ssoValidateTokenUrl;

	@Value("${white.list.urls}")
	private String whiteListingUrls;
	
	@Value("${sso.reg.sso.auth.url}")
	private String ssoSessionRegAuthUrl;
	
	@Value("${user.on.board.registration.request}")
	private String registrationRequestUri;

	private String[] urls = null;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	private UserDetailsServices userdetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String registrationToken = getRegistrationToken(request);
		System.out.println(registrationToken+" "+StringUtils.isEmpty(registrationToken)+" "+ isRegistrationRequest(request)+" "+isValidToken(registrationToken));
		if (!StringUtils.isEmpty(registrationToken) && isRegistrationRequest(request) && isValidToken(registrationToken)) {
			System.out.println("validToken");
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("TEMP_USER",
					"TEMP_PASSWORD", new ArrayList<>());
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(authentication);
		}else if (!isUrlWhiteListed(request)) {
			if (!checkUserAuthenticated(request)) {
				System.out.println("redirecting to login..");
				response.sendRedirect("/login");
				return;
			}
			String userName = tokenUtil.getUsernameFromToken(request.getHeader(TOKEN_HEADER));

			UserDetails userDetails = userdetailsService.loadUserByUsername(userName);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName,
					userDetails.getPassword(), userDetails.getAuthorities());
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * Communicate with SSO to authenticate.
	 * 
	 * @param request
	 * @return
	 */
	public boolean checkUserAuthenticated(HttpServletRequest request) {
		String token = request.getHeader(TOKEN_HEADER);
		System.out.println("Token:"+token);
		if (!StringUtils.isEmpty(token)) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.add(TOKEN_HEADER, token);
			HttpEntity<String> entity = new HttpEntity<String>("", headers);
			ResponseEntity<Response> response = restTemplate.exchange(ssoValidateTokenUrl, HttpMethod.GET, entity,
					Response.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				System.out.println("Status:"+response.getBody().getStatusCode());
				return response.getBody().getStatusCode() == 200 ? true : false;
			} else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				System.out.println("GOt unauthorized");
				return false;
			} else {
				System.out.println("Status:"+response.getBody().getStatusCode());
				return false;
			}
		}
		return false;
	}

	/**
	 * Checks whit listed urls.
	 * 
	 * @param request
	 * @return
	 */
	public boolean isUrlWhiteListed(HttpServletRequest request) {
		if (urls == null)
			urls = whiteListingUrls.split(",");
		for (String url : urls) {
			if (url.equals(request.getRequestURI())) {
				return true;
			}
		}
		return false;
	}

	public String getRegistrationToken(HttpServletRequest request) {
		return request.getHeader("x-registration-token")==null?request.getParameter("x-registration-token"):request.getHeader("x-registration-token");
	}

	public boolean isRegistrationRequest(HttpServletRequest request) {
		System.out.println("/register.json".equals(request.getRequestURI()));
		String[] urls=registrationRequestUri.split(",");
		for(String url:urls) {
			System.out.println(url+" "+request.getRequestURI());
			if(url.equals(request.getRequestURI()))
				return true;
		}
		return false;
	}
	
	public boolean isValidToken(String token) {
		System.out.println("taikg");
		if (!StringUtils.isEmpty(token)) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.add("x-registration-token", token);
			Map<String, String> uriVar=new HashMap<>();
			uriVar.put("x-registration-token", token);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			ResponseEntity<Response> response = restTemplate.getForEntity(ssoSessionRegAuthUrl+"?x-registration-token="+token, Response.class, uriVar);
			if (response.getStatusCode() == HttpStatus.OK) {
				return response.getBody().getStatusCode() == 200 ? true : false;
			} else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				return false;
			} else {
				return false;
			}
		}
		return false;
	}
}
