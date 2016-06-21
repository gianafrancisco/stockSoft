var app = angular.module('mainAdminApp', ['ngRoute','ui.bootstrap','restangular']).
    config(function($routeProvider, $httpProvider) {

             $routeProvider.
               when('/articulo', {
                 templateUrl: '/admin/articulo.html',
                 controller: 'ArticuloController'
               }).
              when('/vendedor', {
                templateUrl: '/admin/vendedor.html',
                controller: 'VendedorController'
              })
            .otherwise({
                 redirectTo: '/articulo'
               });
           $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
           }
)
.controller('VendedorController',vendedorController)
.controller('ArticuloController',articuloController);

app.config(function(RestangularProvider) {

    // add a response interceptor
    RestangularProvider.addResponseInterceptor(function(data, operation, what, url, response, deferred) {
      var extractedData;
      // .. to look for getList operations
      if (operation === "getList") {
        // .. and handle the data and meta data
        //extractedData = data.content;
        extractedData = [];
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