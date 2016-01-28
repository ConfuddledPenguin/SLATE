module.exports = function () {

	var buildDir = '../build/';

	var config = {
		build: {
			buildDir: buildDir,
			cssDir: buildDir + 'style/',
			jsDir: buildDir + 'js/',
			libDir: buildDir + 'lib/'
		},
		sass:{
			srcdir: './style/style.scss'
		},
		js:{
			srcDir: ['./app/**/*.js']
		},
		html:{
			srcDir: ['./app/**/*.html', '!./index.html']
		},
		bower:{
			json: require('./bower.json'),
			srcDir: './bower_components/',
			ignorePath: '../..'
		},
		index: 'index.html',
		ignore:{
			ignoreDir: ['!./bower_components/**/*', '!./node_modules/**/*']
		},
		favicon:{
			src: ['./favicon.ico', './.htaccess']
		},
		fonts: {
			src: ['./fonts/**']
		}
	};

	config.getWiredepDefault = function() {

		var options = {
			bowerJson: config.bower.json,
			directory: config.build.libDir,
			ignorePath: config.bower.ignorePath,
			fileTypes: {
				html: {
					replace: {
						js: function(filePath) {
							return '<script src="/app/' + filePath.replace('../build/', '') + '"></script>';
						},
						css: function(filePath) {
							return '<link rel="stylesheet" href="/app/' + filePath.replace('../build/', '') + '"/>';
						}
					}
				}
			}
		};

		return options;
	};

	config.getInjectDefault = function() {

		var options = {
			addRootSlash: false,
			transform: function(filePath, file, i, length) {

				if(filePath.endsWith('.css')){
					return '<link rel="stylesheet" type="text/css" href="/app/' + filePath.replace('../build/', '') + '">';
				}

				return '<script src="/app/' + filePath.replace('../build/', '') + '"></script>';
			}
		};

		return options;

	};

	return config;

};
