'use strict';

/**
 * @ngdoc function
 * @name stockApp.controller:ReservaController
 * @description
 * # ReservaController
 * Controller of the stockApp
 */
angular.module('stockApp')
  .controller('ReservaController', function ($scope,$http,$window,$location, Restangular, $timeout) {

     var Articulo = Restangular.service('articulos');
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

     $scope.agregarReserva = function(reserva, articulo){

        var exist = false;
        if( reserva.items === undefined){
            reserva.items = [];
        }
        reserva.items.forEach(function(current, index){
            if(current.articulo.articuloId === articulo.articuloId){
                current.cantidad++;
                exist = true;
            }
        });
        if(!exist){
            reserva.items.push({articulo: articulo, cantidad: 1, sinStock: false});
        }
     };

     $scope.generarReserva = function(reserva){
        //TODO: Verificar primero la disponibilidad de los articulos y despues hacer la reserva.
        var res = {descripcion: reserva.descripcion, email: reserva.email};
        var ask = false;
        var msg = "";
        var confirmar = true;
        reserva.items.forEach(function(current,index){
            var params = {
                    page : 0,
                    size : current.cantidad,
                    estado: "DISPONIBLE",
                };
            Articulo.one(current.articulo.articuloId).getList('items', params).then(function(list){
                if(list.length < current.cantidad){
                    current.sinStock = true;
                    ask = true;
                }
            });
        });

        if(ask){
            confirmar = confirm("Alguno articulos no se encuentran disponible en stock. continuar?");
        }
        if(confirmar){        
            Reservas.post(res).then(function(r){
                console.log(r);
                reserva.items.forEach(function(current,index){
                    var params = {
                            page : 0,
                            size : current.cantidad,
                            estado: "DISPONIBLE",
                        };
                    Articulo.one(current.articulo.articuloId).getList('items', params).then(function(list){
                        if(list.length < current.cantidad){
                            alert("La cantidad solicitada del articulo "+current.articulo.codigo+" no se encuentra disponible en stock");
                        }else{
                            list.forEach(function(itemCurr){
                                console.log(itemCurr);
                                itemCurr.reserva = r;
                                itemCurr.estado = "RESERVADO";
                                itemCurr.put();
                            });
                        }
                    });
                });
            });
        }
     };
     $scope.init();
 });