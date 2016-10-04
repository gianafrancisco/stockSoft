'use strict';

/**
 * @ngdoc function
 * @name stockApp.controller:IngresoController
 * @description
 * # IngresoController
 * Controller of the stockApp
 */
angular.module('stockApp')
  .controller('IngresoController', function ($scope, $http, $window, $location, Restangular, $timeout) {

     var Items = Restangular.all('items');

     $scope.tipos = [{id: 0, label: "Virtual"}, {id: 1, label: "Físico"}];
     $scope.tipo = $scope.tipos[0];
     $scope.cantidad = 0;

     $scope.listado = {
     };
     $scope.maxSize = 100;
     $scope.ipp = 50;
     $scope.pageNumber = 1;

     $scope.messageDanger = "";
     $scope.messageSuccess = "";
     $scope.dangerShow = false;
     $scope.successShow = false;


     $scope.showDanger = function (message, show, timeout){
        $scope.messageDanger = message;
        $scope.dangerShow = show;
        if(show == true){
            $timeout($scope.showDanger, timeout, true, "", false, 10);
        }
     };
     $scope.showSuccess = function (message, show, timeout){
        $scope.messageSuccess = message;
        $scope.successShow = show;
        if(show == true){
            $timeout($scope.showSuccess, timeout, true, "", false, 10);
        }
     };

     $scope.buscar = function(){

        var params = {page: $scope.pageNumber - 1, size: $scope.ipp };
        if($scope.search !== "" && $scope.search !== undefined){
            params.ordenDeCompra = $scope.search;
            Items.getList(params).then(function(a){
                $scope.listado = a;
                $scope.pageNumber = $scope.listado.number+1;
            });
        }
     };

     $scope.ingresar = function (item){
        item.tipo = "FISICO";
        item.put();
     };

     $scope.cancelar = function (item){
        item.tipo = "VIRTUAL";
        item.put();
     };

     $scope.esEliminable = function(item){
        return item.estado != "RESERVADO";
     };

     $scope.eliminar = function (item){
        if(confirm("Quiere eliminar la mercaderia?") === true){
            item.remove();
        }
     };

     $scope.init = function(){

     };

     $scope.init();
 });