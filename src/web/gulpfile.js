'use strict'

var gulp = require('gulp');
var sass = require('gulp-sass');
var minifyCSS = require('gulp-clean-css');
var uglify = require('gulp-uglify');
var rename = require('gulp-rename');
var changed = require('gulp-changed');
var clean = require('gulp-clean');

var SCSS_SRC = './src/assets/scss/**/*.scss'
var SCSS_DEST = './src/assets/css'

gulp.task('compile_scss', function() {

	gulp.src(SCSS_SRC)
	.pipe(sass().on('error', sass.logError))
	.pipe(minifyCSS())
	.pipe(rename({suffix: '.min'}))
	// Cannot handle imports :(
//	.pipe(changed(SCSS_DEST))
	.pipe(gulp.dest(SCSS_DEST));
});

gulp.task('build_scss', function() {
    gulp.src(SCSS_DEST)
        .pipe(clean());

    gulp.start('compile_scss')
});

gulp.task('watch_scss', function() {
	gulp.watch(SCSS_SRC, ['compile_scss']);
});

gulp.task('default', ['watch_scss'])