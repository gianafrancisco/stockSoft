'use strict';

/**
 * @ngdoc function
 * @name stockApp.controller:ReservaController
 * @description
 * # ReservaController
 * Controller of the stockApp
 */
angular.module('stockApp')
  .controller('ReservaController', function ($scope,$http,$window,$location, Restangular) {

     var Articulo = Restangular.all('articulos');

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

     $scope.obtenerListaArticulo = function(){

        var params = {page: $scope.pageNumber-1};
        if($scope.search !== "" && $scope.search !== undefined){
            params.search = $scope.search;
        }
        Articulo.getList(params).then(function(a){
             $scope.listado = a;
             $scope.pageNumber = $scope.listado.number+1;
        });

     };

     $scope.buscarArticulo = function(){
         $scope.obtenerListaArticulo();
     };

     $scope.agregarArticulo = function(){
         $scope.articulo.articuloId = null;
         $scope.save(function(){
             $scope.articulo = {};
         });
     };

     $scope.modificar = function(articulo){
         $scope.articulo = articulo;
         var element = $window.document.getElementById('cantidad');
         if(element){
             element.focus();
         }
     };
     $scope.init = function(){
         $scope.obtenerListaArticulo();
     };

     $scope.isModificable = function(){
         return ($scope.articulo.articuloId === null || $scope.articulo.articuloId === undefined);
     };

     $scope.reservarItems = function(articulo, cantidad){
        var item = {estado: 1 };
        Restangular.one("articulos",articulo.articuloId).getList('items',{page: 0,size: cantidad, estado: 1}).then(function(items){
            items.forEach(function(item){
                item.id = item.itemId;
                item.estado = 1;
                item.put();
            });
        });
        $scope.cantidad = 0;
     };

     $scope.init();
 });