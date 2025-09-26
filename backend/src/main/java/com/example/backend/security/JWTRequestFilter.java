//package com.example.backend.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
///**
// * This filter class intercepts incoming HTTP requests to check for a valid JWT (JSON Web Token) in the Authorization header.
// * It ensures that only authenticated and authorized users can access specific endpoints.
// */
//@Component
//public class JWTRequestFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JWTokenUtils tokenUtils;
//
//
//    /**
//     * Performs the actual filtering logic for each incoming HTTP request.
//     *
//     * @param req    The incoming HttpServletRequest.
//     * @param res    The outgoing HttpServletResponse.
//     * @param chain  The FilterChain for continuing the filter process.
//     * @throws ServletException If an exception occurs during the filter process.
//     * @throws IOException      If an I/O exception occurs.
//     */
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
//        // Handle preflight requests
//        if (req.getMethod().equals(HttpMethod.OPTIONS.name())) {
//            handlePreflightRequest(res);
//            return;
//        }
//
//        // Continue with the filter chain for all other requests
//        String encryptedToken = req.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (encryptedToken != null) {
//            try {
//                // Decode the token
//                JWTokenInfo jwTokenInfo = JWTokenUtils.decode(encryptedToken.replace("Bearer", "").trim(), this.tokenUtils.getPassphrase());
//
//                // Access control based on UserType
//                String userRole = jwTokenInfo.isRole();
//                if (!hasAccess(userRole, req.getServletPath())) {
//                    sendForbiddenResponse(res, "Access forbidden.");
//                    return;
//                }
//
//
//            } catch (RuntimeException e) {
//                sendUnauthorizedResponse(res, e.getMessage() + " You need to log in first.");
//                return;
//            }
//        }
//
//        chain.doFilter(req, res);
//    }
//
//
//    /**
//     * Checks if the user with the given role has access to the specified servlet path.
//     *
//     * @param userRole     The role of the user.
//     * @param servletPath  The servlet path of the request.
//     * @return true if the user has access, false otherwise.
//     */
//    private boolean hasAccess(String userRole, String servletPath) {
//        // Assuming the user class has attributes like role, etc.
//        return switch (userRole) {
//            case "admin" -> true; // Admin can access all paths
//            case "user" -> !servletPath.contains("admin"); // User can access paths without "admin"
//            default -> false;
//        };
//    }
//
//
//    /**
//     * Handles preflight requests by setting the appropriate headers.
//     *
//     * @param response The HttpServletResponse for the preflight request.
//     */
//    private void handlePreflightRequest(HttpServletResponse response) {
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080");
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE");
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization, " + HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization");
//        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
//        response.setStatus(HttpServletResponse.SC_OK);
//    }
//
//
//    /**
//     * Sends an unauthorized response with the specified error message.
//     *
//     * @param response The HttpServletResponse for sending the unauthorized response.
//     * @param message  The error message.
//     * @throws IOException If an I/O exception occurs.
//     */
//    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
//    }
//
//
//    /**
//     * Sends a forbidden response with the specified error message.
//     *
//     * @param response The HttpServletResponse for sending the forbidden response.
//     * @param message  The error message.
//     * @throws IOException If an I/O exception occurs.
//     */
//    private void sendForbiddenResponse(HttpServletResponse response, String message) throws IOException {
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
//    }
//
//
//}
//
