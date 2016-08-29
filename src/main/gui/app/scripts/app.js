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
      .when('/reserva', {
        templateUrl: 'views/reserva.html',
        controller: 'ReservaController',
        controllerAs: 'reserva'
      })
      .when('/ingreso', {
        templateUrl: 'views/ingreso.html',
        controller: 'IngresoController',
        controllerAs: 'ingreso'
      })
      .when('/dashboard', {
        templateUrl: 'views/dashboard.html',
        controller: 'DashboardController',
        controllerAs: 'dashboard'
      })
      .when('/usuario', {
        templateUrl: 'views/vendedor.html',
        controller: 'VendedorController',
        controllerAs: 'usuario'
      })
      .otherwise({
        redirectTo: '/dashboard'
      });
  }).config(function(RestangularProvider) {

    // add a response interceptor
    RestangularProvider.addResponseInterceptor(function(data, operation) {
      var extractedData;
      // .. to look for getList operations
      if (operation === "getList") {
        // .. and handle the data and meta data
        //extractedData = data.content;
        if(data.content != undefined){
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
        }else{
          extractedData = data;  
        }
      } else {
        extractedData = data;
      }
      return extractedData;
    });
});
