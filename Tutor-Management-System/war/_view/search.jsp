<!DOCTYPE html>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

		<head>
			<meta charset="UTF-8">
			<meta name="viewport" content="width=device-width, initial-scale=1.0">
			<title>Search View</title>
			<link rel="stylesheet" href="./_view/search.css">
		</head>

		<body>

			<% response.setHeader("cache-control", "no-cache, no-store, must-revalidate" );
				response.setHeader("pragma", "no-cache" ); response.setHeader("expires", "0" ); if
				(session.getAttribute("user")==null) { response.sendRedirect("login.jsp"); } %>

				<div class="super-class background-image"></div>

				<div class="super-class content">

					<h1 class="Header"> Search</h1>

					<form action="${pageContext.servletContext.contextPath}/search" method="get">
						<input type="submit" class="logout" id="logout" name="logout" value="Logout">
					</form>

					<div class="search">
						<form action="${pageContext.servletContext.contextPath}/search" method="post">
							<input type="text" id="searchBox" name="search" value="${searchParameter}">
							<button id="searchBTN" name="searchBTN">&#128269</button>
						</form>
					</div>

					<p id="key">
						&#128196 = Submitted </br>
						&#11088 = New </br>
						&#9989 = Signed </br>
						&#10071 = Edited by Admin
					</p>

					<c:if test="${! empty tutorName}">
						<div class="successAdd">${tutorName} was added as a tutor</div>
					</c:if>
					<c:if test="${! empty editTutorName}">
						<div class="successAdd">${editTutorName} was sucessfully updated</div>
					</c:if>
					<div class="table">
						<table id="searchResults">
							<th>Pay Vouchers</th>
							<th>Tutor Name</th>
							<th>Subject</th>
							<th>Status</th>
							<th>Due Date</th>
							</tr>
							<c:forEach items="${payVouchers}" var="voucher">
								<tr>
									<td>
										<form action="${pageContext.servletContext.contextPath}/search" method="post">
											<input type="submit" id="view" name="viewPayVoucher" value="View"></input>
											<input type="hidden" name="ID" value="${voucher.right.payVoucherID}" />
										</form>
									</td>
									<td> ${voucher.left.name} </td>
									<td> ${voucher.left.subject} </td>
									<td>
										<c:if test="${voucher.right.isSubmitted}">
											&#128196
										</c:if>
										<c:if test="${voucher.right.isNew}">
											&#11088
										</c:if>
										<c:if test="${voucher.right.isSigned}">
											&#9989
										</c:if>
										<c:if test="${voucher.right.isAdminEdited}">
											&#10071
										</c:if>
									</td>
									<td> ${voucher.right.dueDate}</td>
								</tr>
							</c:forEach>
						</table>
					</div>

					<c:if test="${! empty errorMessage}">
						<div class="error">${errorMessage}</div>
					</c:if>
					<form action="${pageContext.servletContext.contextPath}/search" method="post">
						<div class="pageNum">
							<input type="submit" id="previousPageBtn" name="page1" value="&#8592">
							Page ${pageNumber}
							<input type="submit" id="nextPageBtn" name="page2" value="&#8594">
						</div>
					</form>

					<div id="assignVoucher">
						<form action="${pageContext.servletContext.contextPath}/search" method="post">
							<c:choose>
								<c:when test="${user.isAdmin}">
									<input type="submit" id="assignVoucherBtn" name="tutorPage" value="Tutor Page"></input>
								</c:when>
								<c:otherwise>
									<input type="submit" id="assignVoucherBtn" name="tutorProfile" value="View Profile"></input>
								</c:otherwise>
							</c:choose>
						</form>
					</div>
		
				</div>
		</body>

</html>