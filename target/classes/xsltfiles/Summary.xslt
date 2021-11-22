<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:xdt="http://www.w3.org/2005/xpath-datatypes"
	xmlns:err="http://www.w3.org/2005/xqt-errors"
	exclude-result-prefixes="xs xdt err fn">

	<xsl:output method="html" />
	<xsl:decimal-format name="int" NaN="0" />
	<xsl:template match="/">

		<html>
			<link rel='stylesheet' type='text/css' href='./Extras/style.css' />
			<title>HTML REPORTING</title>

			<body>
				<div class='container'>
					<div id='header'>
						<img src='./Logos/client_logo.jpg' class='clientLogo'
							width='100' height='100' />
						<img src='./Logos/Indium-Software-Logo.jpg' class='indiumLogo' />
						<h1>iSafe</h1>
					</div>
					<br />
					<br />
					<div id='exectable' style='clear:both;'>
						<table class='column-options'>
							<tr>
								<th>Test case no</th>
								<th>Test case name</th>
								<th>Test case description</th>
								<th>Duration</th>
								<th>Status</th>
							</tr>
							<xsl:for-each select="//Scenario">
								<xsl:apply-templates select="TestCase" />
							</xsl:for-each>
						</table>
					</div>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="TestCase">

		<tr>
			<td>
				<xsl:number />
			</td>
			<TD>
				<a target=''>
					<xsl:attribute name="href">
    						<xsl:value-of
						select="concat('./Resultfiles/','@id','.html')" />
  					</xsl:attribute>
					<xsl:value-of select="@id" />
				</a>
			</TD>
			<td>
				<xsl:value-of select="@description" />
			</td>
			<td>
				<xsl:value-of select="@duration" />
			</td>
			<xsl:if test="@status='Failed'">
				<TD style='color:red'>Fail</TD>
			</xsl:if>
			<xsl:if test="@status='Passed'">
				<TD style='color:green'>Pass</TD>
			</xsl:if>
			<xsl:if test="@status='Pending'">
				<TD style='color:blue'>Pending</TD>
			</xsl:if>
		</tr>
	</xsl:template>

</xsl:stylesheet>
