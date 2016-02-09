/*
 * This is the gulpfile for SLATE's SPA client
 */

//////////////////////////////////////////////////
//Set up

var gulp    = require('gulp'),
	$       = require('gulp-load-plugins')({lazy: true}), //best plugin ever it lazy loads other plugins!
	config  = require('./gulp-config')(),
	del = require('del'),
	htmlreplace = require('gulp-html-replace');

//////////////////////////////////////////////////
//Clear Build folder

gulp.task('clearbuild', function(cb){
	log("Cleaning build folder - " + config.build.buildDir);

	del(config.build.buildDir + "**/*", {force: true}).then(function () {
		cb();
	});
});

/////////////////////////////////////////////////
// Build SASS

gulp.task('sass', ['clearbuild'], function() {

	log('Compiling SASS');

	return gulp
		.src(config.sass.srcdir)
		.pipe($.sass({errLogToConsole: true}))
		.pipe(gulp.dest(config.build.cssDir));
});

/////////////////////////////////////////////////
// Handle Dependencies

gulp.task('handleDep', ['clearbuild', 'libcopy', 'dep'], function() {

	log('Dependencies handled');
});

gulp.task('libcopy',['clearbuild'], function(){

	log("Copying Bower Dependencies to - " + config.build.libDir);

	return gulp
		.src(config.bower.srcDir + '**/*.*')
		.pipe(gulp.dest(config.build.libDir));

});

gulp.task('dep', ['clearbuild', 'libcopy', 'sass', 'copy'], function() {

	log('Injecting dependencies for:' +
		' \n\t\t-bower components ' +
		' \n\t\t-custom js' +
		' \n\t\t-custom css');

	var options = config.getWiredepDefault();
	var wiredep = require('wiredep').stream;

	return gulp
		.src(config.index)
		.pipe(wiredep(options))
		.pipe($.inject(gulp.src([config.build.buildDir + 'app/**/*.module.js', config.build.buildDir + 'app/**/*.js', '!' + config.build.buildDir + 'app/app.js']), config.getInjectDefault()))
		.pipe($.inject(gulp.src(config.build.cssDir + '**/*.css'), config.getInjectDefault()))
		.pipe(gulp.dest(config.build.buildDir));
});

/////////////////////////////////////////////////
// Copy everything else

gulp.task('copy', ['clearbuild', 'copyfavicon', 'copyFonts'], function() {

	log('Copying App to ' + config.build.builddir);

	return gulp
		.src(config.js.srcDir.concat(config.html.srcDir).concat(config.ignore.ignoreDir))
		.pipe(gulp.dest(config.build.buildDir + "app/"));
});

gulp.task('copyfavicon', ['clearbuild'], function() {

	log('Copying App to ' + config.build.builddir);

	return gulp
		.src(config.favicon.src)
		.pipe(gulp.dest(config.build.buildDir));
});

gulp.task('copyFonts', ['clearbuild'], function() {

	return gulp
		.src(config.fonts.src)
		.pipe(gulp.dest(config.build.buildDir + '/fonts/'));
});

/////////////////////////////////////////////////
// Perform full build

gulp.task('build',['clearbuild', 'sass', 'handleDep', 'copy'], function(){


	gulp.src(config.build.indexfile)
		.pipe(htmlreplace({
			'base': '<base href="/app/" />'
		}))
		.pipe(gulp.dest(config.build.buildDir));

	log($.util.colors.green('Build Complete'));
});

gulp.task('release',['clearbuild', 'sass', 'handleDep', 'copy'], function(){

	gulp.src(config.build.indexfile)
		.pipe(htmlreplace({
			'base': '<base href="/" />'
		}))
		.pipe(gulp.dest(config.build.buildDir));


	log($.util.colors.green('Build Complete'));
});

/////////////////////////////////////////////////
/*
 * Handy helper functions of helpfulness
 */

function log(msg) {
	if (typeof(msg) === 'object') {
		for (var item in msg) {
			if (msg.hasOwnProperty(item)) {
				$.util.log($.util.colors.blue(msg[item]));
			}
		}
	} else {
		$.util.log($.util.colors.blue(msg));
	}
}


if (!String.prototype.endsWith) {
	String.prototype.endsWith = function(searchString, position) {
		var subjectString = this.toString();
		if (typeof position !== 'number' || !isFinite(position) || Math.floor(position) !== position || position > subjectString.length) {
			position = subjectString.length;
		}
		position -= searchString.length;
		var lastIndex = subjectString.indexOf(searchString, position);
		return lastIndex !== -1 && lastIndex === position;
	};
}