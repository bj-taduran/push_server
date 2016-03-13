basePath = '../';

files = [
  JASMINE,
  JASMINE_ADAPTER,
  '../webapp/static/js/libs/*.js',
  'lib/angular/angular-mocks-*.js',
  '../webapp/static/js/page/*.js',
  'unit/**/*.js'
];

autoWatch = true;

browsers = ['Chrome'];

junitReporter = {
  outputFile: 'test_out/unit.xml',
  suite: 'unit'
};
