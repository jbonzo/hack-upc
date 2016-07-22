var gulp   = require('gulp');
var minify = require('gulp-minify');
var concat = require('gulp-concat');
var rename = require('gulp-rename');
// var clean  = require('gulp-clean');

gulp.task('default', function () {
  gulp.src(['lib/*.js', 'lib/*/*/*.js'])
  	.pipe(
  		concat('app.js')
  	)
    // .pipe(
    // 	minify({
    //       mangle: false,
	   //      ext:{
	   //          src:'.debug.js',
	   //          min:'.min.js'
	   //      },
	   //      exclude: ['tasks'],
	   //      ignoreFiles: ['-min.js']
	   //  })
    // )
    .pipe(
    	gulp.dest('dist')
    );
  gulp.src(['lib/*/*.css', 'lib/*.html', 'lib/*/*/*.html'])
    .pipe(
      rename({dirname: ''})
    )
    .pipe(
      gulp.dest('dist')
    );
});
