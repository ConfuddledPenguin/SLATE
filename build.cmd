ECHO Building Documentation

CALL cd Server

CALL apidoc -i src/ -o ../../Doc/apiDoc
CALL javadoc -sourcepath src/main/java/ -d ../../Doc/javaDoc/ -subpackages com.tom_maxwell.project -windowtitle SLATE -doctitle "SLATE: Strathclyde Learning Analytics Test Environment" -bottom "<font size='1.5rem' >SLATE is the dissertation project of <a href='http://tom-maxwell.com'> Thomas Maxwell</a>. The GitHub repository can be found <a href='https://github.com/ConfuddledPenguin/SLATE'> here </a></font>" -header "<b>SLATE</b>"

CALL cd ..