var app = angular.module('mainAdminApp', ['ngRoute','ui.bootstrap']).
    config(function($routeProvider, $httpProvider) {

             $routeProvider.
               when('/articulo', {
                 templateUrl: '/admin/articulo.html',
                 controller: 'ArticuloController'
               }).
              when('/vendedor', {
                templateUrl: '/admin/vendedor.html',
                controller: 'VendedorController'
              }).
              when('/venta', {
                templateUrl: '/admin/venta.html',
                controller: 'VentaController'
              })
            .otherwise({
                 redirectTo: '/articulo'
               });
           $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
           }
)
.controller('VentaController',ventaController)
.controller('VendedorController',vendedorController)
.controller('ArticuloController',articuloController);
