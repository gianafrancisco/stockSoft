function mainController($scope,$http,$window,$location, $rootScope) {

     $scope.isOpen=function(){
          $http.get("/caja/abierta")
          .success(function(data, status, headers, config) {
              $rootScope.cajaAbierta=data;
          });
     };
     $scope.init = function(){
        $scope.isOpen();
     };
     $scope.init();

};
var app = angular.module('mainApp', ['ngRoute','ui.bootstrap']).
    config(function($routeProvider, $httpProvider) {

             $routeProvider.
               when('/articulo', {
                 templateUrl: 'articulo.html',
                 controller: 'ArticuloController'
               }).
              when('/vender', {
                templateUrl: 'vender.html',
                controller: 'VenderController'
              }).
              when('/venta', {
                templateUrl: 'venta.html',
                controller: 'VentaController'
              }).
              when('/retiro', {
                  templateUrl: 'retiro.html',
                  controller: 'RetiroController'
              }).
              when('/caja', {
                  templateUrl: 'caja.html',
                  controller: 'CajaController'
              })
            .otherwise({
                 redirectTo: '/caja'
               });
           $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
           }
)
.controller('MainController',mainController)
.controller('VentaController',ventaController)
.controller('VenderController',venderController)
.controller('RetiroController',retiroController)
.controller('CajaController',cajaController)
.controller('ArticuloController',articuloController);



