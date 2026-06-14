<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="member.aspx.cs" Inherits="web._default" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
<title><%=System.Configuration.ConfigurationManager.AppSettings["sitetitle"] %></title>
<link href="images/style.css" rel="stylesheet" type="text/css" /> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<meta http-equiv="Expires" content="-1">           
<meta http-equiv="Cache-Control" content="no-cache">           
<meta http-equiv="Pragma" content="no-cache">           
<link rel="stylesheet" type="text/css" href="images/base2013.css" charset="gbk">
<link rel="stylesheet" type="text/css" href="images/index.css" charset="gbk">	
<link rel="stylesheet" type="text/css" href="css/mem_main.css" charset="gbk">
<script type="text/javascript" src="images/jquery-1.6.2.min.js"></script>
<script src="images/zepto.min.js"></script>
<script type="text/javascript">window.jQuery = window.Zepto;</script>
<script type="text/javascript" src="images/common.js"></script> 
<!-- Link Swiper's CSS --><link rel="stylesheet" href="css/swiper.min.css">
<!-- Demo styles -->
<link rel="stylesheet" type="text/css" href="boss.css" charset="gbk">
</head>
<body class="bodybg2">
	<!--<div class="title">
		<a href="javascript :;" onClick="javascript :history.back(-1);"></a>
		<h3>会员中心</h3>
	</div>-->
	<div class="head">
		<a href="reinfo.aspx" class="toux"><img src="./images/defaul.png" id="myphoto" runat=server></a>
		<div class="head_r">
			<p class="name" id="truename" runat="server"></p>
			<p class="account" id="user_name" runat="server"></p>
			<p id="user_level" style="display: none;" runat="server"></p>
			
		</div>
		<a href="logout.aspx" class="out">退出</a>
	</div>
	<div class="gundong">
		<div class="gundong_z">
		<span class="gdpic"></span>
		<marquee loop="-1" scrollamount="2" bgcolor="#fff" direction="left" id="marquee1" runat="server">
		</marquee>
		</div>
	</div>
	<div class="zhuti">
		<ul>
			<%--<li>
				<a href="adduser.aspx?upid=<%=Session["adminusername"].ToString() %>" onfocus="this.blur();">
					<img src="images/ico1.png" />
					<h2>推荐注册</h2>
				</a>
			</li>--%>
			<li>
				<a href="sepass.aspx?act=actuser" onfocus="this.blur();">
					<img src="images/ico1.png" />
					<h2>激活会员</h2>
				</a>
			</li>
			<li>
				<a href="myadduser.aspx" onfocus="this.blur();">
					<img src="images/ico2.png" />
					<h2>我的团队</h2>
				</a>
			</li>
			<li>
				<a href="sepass.aspx?act=usergroup" onfocus="this.blur();">
					<img src="images/ico3.png" />
					<h2>系谱图</h2>
				</a>
			</li>
			<li>
				<a href="paylog.aspx">
					<img src="images/ico4.png" />
					<h2>奖金明细</h2>
				</a>
			</li>
			<li>
				<a href="sepass.aspx?act=moneytobaodan" onfocus="this.blur();">
					<img src="images/ico5.png" />
					<h2>转报单币</h2>
				</a>
			</li>
			<li>
				<a href="sepass.aspx?act=zhuanmoney" onfocus="this.blur();">
					<img src="images/ico6.png" />
					<h2>交易平台</h2>
				</a>
			</li>
			<li>
				<a href="sepass.aspx?act=tixian" onfocus="this.blur();">
					<img src="images/ico7.png" />
					<h2>我要提现</h2>
				</a>
			</li>
			<li>
				<a href="reinfo.aspx">
					<img src="images/ico8.png" />
					<h2>我的资料</h2>
				</a>
			</li>
			<li>
				<a href="repass.aspx">
					<img src="images/ico9.png" />
					<h2>修改密码</h2>
				</a>
			</li>
			<li>
				<a href="infolist.aspx?kind=2">
					<img src="images/ico10.png" />
					<h2>活动公告</h2>
				</a>
			</li>
			<li>
				<a href="gbook.aspx">
					<img src="images/ico11.png" />
					<h2>留言反馈</h2>
				</a>
			</li>
			<%--<li>
				<a href="mem_haibao.aspx">
					<img src="images/ico13.png" />
					<h2>我的二维码</h2>
				</a>
			</li>--%> 
			<div class="clear"></div>
		</ul>
	</div>
	<div class="gao"></div>
	
	<div class="bottom">
		<ul>
			<li>
				<a href="member.aspx">
					<img src="images/ico71.png" />
					<h2>首页</h2>
				</a>
			</li>
			<li>
				<a href="sepass.aspx?act=zhuanmoney">
					<img src="images/ico81.png" />
					<h2>交易平台</h2>
				</a>
			</li>
			<li>
				<a href="infolist.aspx?kind=2">
					<img src="images/ico91.png" />
					<h2>平台活动</h2>
				</a>
			</li>
			<li>
				<a href="reinfo.aspx">
					<img src="images/ico61.png" />
					<h2>账户设置</h2>
				</a>
			</li>
			<div class="clear"></div>
		</ul>
	</div>
</body> 
</html>
