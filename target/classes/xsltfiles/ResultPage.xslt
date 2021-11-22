<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:xdt="http://www.w3.org/2005/xpath-datatypes"
	xmlns:err="http://www.w3.org/2005/xqt-errors" exclude-result-prefixes="xs xdt err fn">

	<xsl:output method="html" />
	<xsl:decimal-format name="int" NaN="0" />
	<xsl:param name="testCaseID" />
	<xsl:template match="/">

		<html>
			<head>
				<title>Report Description</title>
				<link rel='stylesheet' type='text/css' href='../../Extras\style.css' />
				<style>
					.ReportPass
					{
					color:#006400;
					}

					.ReportFail
					{
					color:red;
					}
					.ReportInfo
					{
					color:blue;
					}
					.ReportVerify
					{
					color:blue;
					}
				</style>
			</head>
			<body>

				<div id="ReportRightcolumn" class="rightcontent">
					<div id="{//TestCase[@id=$testCaseID]/@id}">
						<h1 align="center" style="color:brown;">
							<xsl:value-of select="//TestCase[@id=$testCaseID]/@id" />
							:
							<xsl:value-of select="//TestCase[@id=$testCaseID]/@description" />
						</h1>
						<hr />
						<div align="left">
							<table width="80%">
								<tr style="color:brown;">
									<td style='width: 10%'>
										<b>S.No</b>
									</td>
									<td style='width: 65%'>
										<b>Description</b>
									</td>
								</tr>
								<xsl:for-each select="//TestCase[@id=$testCaseID]">
									<xsl:apply-templates select="TestStep" />
								</xsl:for-each>
							</table>
						</div>
					</div>
				</div>
				<div id='buttonsar'>
					<a class='btn' href='../SummaryPage.html'>Click here to see the Summary page</a>
					<a class='btn' href='../../Report.html'>Click here to see the dashboard report</a>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="TestStep">

		<tr>
			<td style='width: 10%'>
				<xsl:number />
			</td>
			<xsl:if test="@Status='Fail'">
				<td style='width: 80%'>
					<font class="ReportFail">
						<xsl:value-of select="@Message" />
					</font>
				</td>
			</xsl:if>
			<xsl:if test="@Status='Pass'">
				<td style='width: 80%'>
					<font class="ReportPass">
						<xsl:value-of select="@Message" />
					</font>
				</td>
			</xsl:if>
			<xsl:if test="@Status='Info'">
				<td style='width: 80%'>
					<font class="ReportInfo">
						<xsl:value-of select="@Message" />
					</font>
				</td>
			</xsl:if>
			<xsl:if test="@Status='Verify'">
				<td style='width: 80%'>
					<font class="ReportVerify">
						<xsl:value-of select="@Message" />
					</font>
				</td>
			</xsl:if>
		</tr>
		<xsl:if test="@Status='Fail'">
			<tr>
				<td style='width: 10%'></td>
				<td style='width: 80%'></td>
				<td style='width: 10%'>
					<xsl:if test="@ScreenShotName!=''">
						<a href="{@ScreenShotName}.png">
							<img src="{@ScreenShotName}.png" width="100" height="150" />
						</a>
					</xsl:if>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="@Status='Pass'">
			<tr>
				<td style='width: 10%'></td>
				<td style='width: 80%'></td>
				<td style='width: 10%'>
					<xsl:if test="@ScreenShotName!=''">
						<a href="{@ScreenShotName}.png">
							<img src="{@ScreenShotName}.png" width="100" height="150" />
						</a>
					</xsl:if>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="@Status='Info'">
			<tr>
				<td style='width: 10%'></td>
				<td style='width: 80%'></td>
				<td style='width: 10%'>
					<xsl:if test="@ScreenShotName!=''">
						<a href="{@ScreenShotName}.png">
							<img src="{@ScreenShotName}.png" width="100" height="150" />
						</a>
					</xsl:if>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="@Status='Verify'">
			<tr>
				<td style='width: 10%'></td>
				<td style='width: 80%'></td>
				<td style='width: 10%'>     
					<xsl:if test="@ScreenShotName!=''">
						<a href="{@ScreenShotName}.png"> <img src="{@ScreenShotName}.png" width="100" height="150"/> </a>
					</xsl:if>
				</td>
			</tr>
		</xsl:if>

	</xsl:template>

</xsl:stylesheet>
