<%@ include file="../../common/header.jspf"%>
<%@ include file="../../common/navigation.jspf"%>
<link rel="stylesheet" 
	  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.3/jquery.validate.js"></script>
<div class="container">
	<form:form action="add" method="post" modelAttribute="StockAlerts">
		<form:hidden path="id" />
		<form:hidden path="user.id" value="${user.id}"/>
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
				<div class="row">
					<div class="col-md-10">
						<form:label path="stock.lastPrice" class="form-label">Last Price</form:label>
						<form:input path="stock.lastPrice" class="form-control"	readonly="true" />
						<form:errors path="stock.lastPrice" class="text-warning" />				
					</div>
					<div class="col-md-1" style="margin-left: -20px;margin-top: 2.2rem;">
						<a onclick="javascript:getLastPrice();"><i id="refresh" class="fa fa-refresh" style="font-size: 30px;"></i></a>
					</div>
				</div>
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

	$(document).ready(function(){
		
		$("#StockAlerts").validate({
			rules : {
				"stock\\.symbol" : "required",
				alertPrice : "required"
			},
			messages : {
				"stock\\.symbol" : "Please Enter Symbol",
				alertPrice : "Please Enter Alert Price"
			},
			submitHandler: function (form) {
				form.submit();
			}
		})
	});


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
// 					console.log(data);
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
		$('#refresh').attr("class","fa fa-refresh fa-spin");
		$.ajax({
			url : '${pageContext.request.contextPath}/live/lastPrice',
			datatype : 'json',
			data : {
				symbol : symbol
			},
			success : function(res) {
				$('#stock\\.lastPrice').val(res)
				$('#refresh').attr("class","fa fa-refresh")
			},
			error : function(err) {
				console.log('error in getting live price --> ' + err)
				$('#refresh').attr("class","fa fa-refresh")
			}
		})
	}
	
	function getLastPrice(){
		var symbol = $('#stock\\.symbol').val();
		if (symbol && symbol.trim() != "") {
			setLastPrice(symbol);
		} else {
			alert("Please Select Symbol")
		}
	}
</script>
<%@ include file="../../common/footer.jspf"%>