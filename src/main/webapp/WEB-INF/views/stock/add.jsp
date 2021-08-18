<%@ include file="../../common/header.jspf"%>
<%@ include file="../../common/navigation.jspf"%>

<div class="container">
	<form:form action="add" method="post" modelAttribute="StockAlerts">
		<form:hidden path="id" />
		<form:hidden path="user.id" />
		<form:hidden path="stock.id" />
		<div class="row mb-3">
			<fieldset class="form-group col-md-6">
				<form:label path="stock.symbol" class="form-label">Symbol</form:label>
				<form:input path="stock.symbol" class="form-control" readonly="${ id != null ? 'readonly' : ''}"/>
				<form:errors path="stock.symbol" class="text-warning" />
			</fieldset>
			<fieldset class="form-group col-md-6">
				<form:label path="stock.companyName" class="form-label">Company Name</form:label>
				<form:input path="stock.companyName" class="form-control"
					readonly="true" />
				<form:errors path="stock.companyName" class="text-warning" />
			</fieldset>
		</div>
		<div class="row mb-3">
			<fieldset class="form-group col-md-4">
				<form:label path="stock.lastPrice" class="form-label">Last Price</form:label>
				<form:input path="stock.lastPrice" class="form-control"
					readonly="true" />
				<form:errors path="stock.lastPrice" class="text-warning" />
			</fieldset>
			<fieldset class="form-group col-md-4">
				<form:label path="alertPrice" class="form-label">Alert Price</form:label>
				<form:input path="alertPrice" class="form-control" />
				<form:errors path="alertPrice" class="text-warning" />
			</fieldset>
			<fieldset class="form-group col-md-4">
				<form:label path="targetPrice" class="form-label">Target Price</form:label>
				<form:input path="targetPrice" class="form-control" />
				<form:errors path="targetPrice" class="text-warning" />
			</fieldset>
		</div>
		<div class="row mb-3">
			<fieldset class="form-group col-md-4">
				<form:label path="AlertEnabled" class="form-label">Active Alert:  </form:label>
				<form:radiobutton path="AlertEnabled" value="true" />Yes
				<form:radiobutton path="AlertEnabled" value="false" />No
			</fieldset>
			<fieldset class="form-group col-md-4">
				<form:label path="MailSend" class="form-label">Mail Send:  </form:label>
				<form:radiobutton path="MailSend" value="true" />Yes
				<form:radiobutton path="MailSend" value="false" />No
			</fieldset>
		</div>
		<input type="submit" class="btn btn-primary" value="Submit">
	</form:form>
</div>
<script type="text/javascript">
	$("#stock\\.symbol").autocomplete({
		source : function(request, response) {
			$.ajax({
				url : "${pageContext.request.contextPath}/live/search",
				dataType : 'json',
				data : {
					q : request.term
				},
				success : function(data) {
					$('#stock\\.companyName').val('')
					console.log(data);
					response(data);
				},
				error : function(err) {
					$('#stock\\.companyName').val('')
					console.log("error in ajax search call: " + err)
				}
			});
		},
		select : function(event, ui) {
			this.value = ui.item.symbol
			$('#stock\\.companyName').val(ui.item.symbol_info)
			setLastPrice(ui.item.symbol)
			return false;
		}
	}).data("ui-autocomplete")._renderItem = function(ul, item) {
		return $("<li>").append(
				"<a>" + item.symbol_info + "    <strong>" + item.symbol
						+ "</strong></a>").appendTo(ul);
	};

	function setLastPrice(symbol) {
		$.ajax({
			url : '${pageContext.request.contextPath}/live/lastPrice',
			datatype : 'json',
			data : {
				symbol : symbol
			},
			success : function(res) {
				$('#stock\\.lastPrice').val(res)
			},
			error : function(err) {
				console.log('error in getting live price --> ' + err)
			}
		})
	}
</script>
<%@ include file="../../common/footer.jspf"%>