<%@ include file="../../common/header.jspf"%>
<%@ include file="../../common/navigation.jspf"%>


<div class="container-fluid">
	<h2>Stocks Added by you</h2>
	<c:if test="${not empty message}">
		<div class="${messageCss}" role="alert">
		    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-exclamation-triangle-fill flex-shrink-0 me-2" viewBox="0 0 16 16" role="img" aria-label="Warning:">
   				 <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-3.97-3.03a.75.75 0 0 0-1.08.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-.01-1.05z"/>
 			</svg>
			${message}
			<button type="button" class="btn-close" data-bs-dismiss="alert"
				aria-label="Close"></button>
		</div>
	</c:if>
	<table class="table table-striped" id="stockDatatable">
		<thead>
			<tr>
				<th>Sr. No</th>
				<th>Symbol</th>
				<th>Company Name</th>
<!-- 				<th>Series</th> -->
				<th>Alert Price</th>
<!-- 				<th>Target Price</th> -->
				<th>Alert Difference (%)</th>
				<th>Live Price</th>
				<th>Added Date</th>
				<th>Last Updated Date</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty stockAlertList }">
				<tr>
					<td colspan="10" class="text-center">No Stocks Added Yet.. <a
						href="add">click here</a> to Add Stocks In your Alert List
					</td>
				</tr>
			</c:if>
			<c:if test="${not empty stockAlertList }">
				<c:forEach items="${stockAlertList}" var="alerts" varStatus="counter">
					<tr>
						<td>${counter.count}</td>
						<td>${alerts.stock.symbol}</td>
						<td>${alerts.stock.companyName}</td>
<%-- 						<td>${stock.series}</td> --%>
						<td>${alerts.alertPrice}</td>
<%-- 						<td>${alerts.targetPrice}</td> --%>
						<td>
							<c:if test="${alerts.alertDiff != null}">${alerts.alertDiff} %</c:if> 
						</td>
						<td>${alerts.stock.lastPrice}</td>
						<td>${alerts.createdDate}</td>
						<td>${alerts.updatedDate}</td>
						<td><a
							href="${pageContext.request.contextPath}/saved/update?id=${alerts.id}"
							class="btn btn-success btn-sm">Update</a> <a
							href="${pageContext.request.contextPath}/saved/delete?id=${alerts.id}&symbol=${alerts.stock.companyName}"
							class="btn btn-danger btn-sm">Delete</a></td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
</div>
<script type="text/javascript">
$(document).ready(function() {
    $('#stockDatatable').DataTable();
} );
</script>
<%@ include file="../../common/footer.jspf"%>