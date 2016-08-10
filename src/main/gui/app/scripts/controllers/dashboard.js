'use strict';

/**
 * @ngdoc function
 * @name stockApp.controller:DashboardCtrl
 * @description
 * # DashboardCtrl
 * Controller of the stockApp
 */
angular.module('stockApp')
  .controller('DashboardController', function ($scope, Restangular, $timeout) {
     var Reservas = Restangular.service('reservas');

     $scope.reserva = {
        descripcion: "",
        email: "",
        items: []
     };
     
     $scope.listado = {
         numberOfElements: 0,
         number: 0,
         totalElements: 0
     };
     $scope.maxSize = 100;
     $scope.listadoCodigo = {};
     $scope.articulo = {};
     $scope.codigo = {};
     $scope.ipp = 20;
     $scope.pageNumber = 1;
     $scope.cantidad = 0;
     $scope.descripcionError = false;
     $scope.emailError = false;
     $scope.messageSuccess = "";
     $scope.successShow = false;

     $scope.showSuccess = function (message, show, timeout){
        $scope.messageSuccess = message;
        $scope.successShow = show;
        if(show == true){
            $timeout($scope.showSuccess, timeout, true, "", false, 10);
        }
     };

     $scope.buscar = function(buscar){

        var params = {page: $scope.pageNumber-1, sort: 'id,desc'};
        if(buscar !== "" && buscar !== undefined){
            params.search = buscar;
        }
        Reservas.getList(params).then(function(a){
             $scope.listado = a;
             $scope.pageNumber = $scope.listado.number+1;
        });

     };
     $scope.cancelar = function(reserva){
     	reserva.estado = "CANCELADA";	
     	reserva.put();
     };

     $scope.cerrar = function(reserva){
     	reserva.estado = "CERRADA";	
     	reserva.put();
     };

     $scope.init = function(){
        $scope.buscar("");
     };

     $scope.isModificable = function(){
         return ($scope.articulo.articuloId === null || $scope.articulo.articuloId === undefined);
     };

     $scope.mostrarAcciones = function(reserva){
     	return reserva.estado === "ACTIVA" || reserva.estado === "CONFIRMADA";
     };

     $scope.init();
  });
