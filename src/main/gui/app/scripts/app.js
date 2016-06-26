'use strict';

/**
 * @ngdoc overview
 * @name stockApp
 * @description
 * # stockApp
 *
 * Main module of the application.
 */
angular
  .module('stockApp', [
    'ngAnimate',
    'ngAria',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'restangular'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/articulo', {
        templateUrl: 'views/articulo.html',
        controller: 'ArticuloController',
        controllerAs: 'articulo'
      })
      .when('/vendedor', {
        templateUrl: 'views/vendedor.html',
        controller: 'VendedorController',
        controllerAs: 'vendedor'
      })
      .otherwise({
        redirectTo: '/articulo'
      });
  }).config(function(RestangularProvider) {

    // add a response interceptor
    RestangularProvider.addResponseInterceptor(function(data, operation) {
      var extractedData;
      // .. to look for getList operations
      if (operation === "getList") {
        // .. and handle the data and meta data
        //extractedData = data.content;
        extractedData = data.content;
        extractedData.content = data.content;
        extractedData.number = data.number;
        extractedData.totalPages = data.totalPages;
        extractedData.totalElements = data.totalElements;
        extractedData.last = data.last;
        extractedData.size = data.size;
        extractedData.sort = data.sort;
        extractedData.first = data.first;
        extractedData.numberOfElements = data.numberOfElements;
      } else {
        extractedData = data;
      }
      return extractedData;
    });
});
