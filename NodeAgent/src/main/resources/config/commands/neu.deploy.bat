<?xml version="1.0" encoding="UTF-8"?>
<project name="deploy" default="neu-deploy">
	<target name="neu_download">
		<ftp server="{address}" port="{port}" userid="{user}" password="{password}" remotedir="{remoteDir}" verbose="yes" action="get">
			<fileset dir="{deployDir}">
				<include name="{name}"/>
			</fileset>
		</ftp>
	</target>
	<target name="neu-deploy" depends="neu_download">
		<unzip src="{deployDir}/{name}" dest="{deployDir}"/>
	</target>
</project>