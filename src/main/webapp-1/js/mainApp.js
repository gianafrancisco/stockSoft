function mainController($scope,$http,$window,$location, $rootScope) {
};
var app = angular.module('mainApp', ['ngRoute','ui.bootstrap','restangular']).
    config(function($routeProvider, $httpProvider) {

             $routeProvider.
               when('/articulo', {
                 templateUrl: 'articulo.html',
                 controller: 'ArticuloController'
               })
            .otherwise({
                 redirectTo: '/articulo'
               });
           $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
           }
)
.controller('MainController',mainController)
.controller('ArticuloController',articuloController);



