'use strict';

/**
 * @ngdoc function
 * @name stockApp.controller:ArticuloController
 * @description
 * # ArticuloController
 * Controller of the stockApp
 */
angular.module('stockApp')
  .controller('ArticuloController', function ($scope,$http,$window,$location, Restangular, $timeout) {

     var Articulo = Restangular.all('articulos');

     $scope.tipos = [{id: 0, label: "Virtual"}, {id: 1, label: "Físico"}];
     //$scope.monedas = [{id: 0, label: "DOLAR"}, {id: 1, label: "Euro"}, {id: 2, label: "Peso"}];
     $scope.monedas = ["DOLAR", "EURO","PESO"];
     $scope.tipo = $scope.tipos[0];
     $scope.cantidad = 0;

     $scope.listado = {
         numberOfElements: 0,
         number: 0,
         totalElements: 0
     };
     $scope.maxSize = 100;
     $scope.listadoCodigo = {};
     $scope.articulo = {};
     $scope.codigo = {};
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

     $scope.obtenerListaArticulo = function(){

        var params = {page: $scope.pageNumber-1, size: $scope.ipp };
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
     $scope.limpiar = function(){
         $scope.articulo = {};
         $scope.articulo.moneda = $scope.monedas[0];
     };
     $scope.modificarArticulo = function(){
         $scope.save(function(){
                $scope.limpiar();
         });
     };
     $scope.modificar = function(articulo){
         $scope.articulo = articulo;
     };
     $scope.copiar = function(articulo){
         $scope.modificar(articulo);
         $scope.articulo.articuloId = undefined;
         $scope.articulo.codigo = "";
         $scope.articulo.cantidadStock = 0;
         $scope.articulo.cantidad = 0;
         var element = $window.document.getElementById('codigo');
         if(element){
             element.focus();
         }
     };
     $scope.save = function(callback){

        if($scope.articulo.articuloId === null){
            if($scope.articulo.codigo === "" || $scope.articulo.codigo === undefined){
                alert("Debe ingresar el código de articulo para poder seguir.");
                return;
            }
            var params = {codigo: $scope.articulo.codigo };
            
            Articulo.getList(params).then(function(a){
                //alert("El codigo artículo que intenta cargar ya existe en la base de datos.");
                if(confirm("El codigo artículo que intenta cargar ya existe en la base de datos.\nQuiere utilizar el artículo cargado previamente ?")){
                    console.log(a);
                    $scope.modificar(a[0]);
                }else{
                    $scope.articulo.codigo = "";
                }
            },
            function (err){
                if(err.status === 404){
                    Articulo.post($scope.articulo).then(function(res){
                        if($scope.cantidad > 0 ){
                            $scope.agregarItems(res, $scope.cantidad, $scope.tipo, $scope.ordenDeCompra);
                        }
                        $scope.obtenerListaArticulo();
                        if(callback !== undefined){
                            callback();
                        }
                    });
                }
            });
        }else{
            if($scope.articulo.codigo === "" || $scope.articulo.codigo === undefined){
                alert("Debe ingresar el código de articulo para poder seguir.");
                return;
            }
            $scope.articulo.put().then(function(){
                 if(callback !== undefined){
                     callback();
                 }
            });
        }
     };

     $scope.eliminar = function(articulo){
         if(confirm("Esta seguro que quiere elimiar el articulo?")){
            Restangular.one("articulos", articulo.articuloId).remove().then(function(){
                $scope.obtenerListaArticulo();
            });
         }
     };
     $scope.init = function(){
         $scope.obtenerListaArticulo();
     };

     $scope.isModificable = function(){
         return ($scope.articulo.articuloId === null || $scope.articulo.articuloId === undefined);
     };

     $scope.cerrarSession = function(){
         $http.post('/logout', {}).then(function() {
             $location.path("/index.html");
         });
     };
     $scope.itemACargar = function(){
        return !$scope.isModificable() && $scope.cantidad > 0;
     };
     $scope.agregarItems = function(articulo, cantidad, tipo, ordenDeCompra){
        var item = {
                tipo: tipo.id,
                ordenDeCompra: ordenDeCompra,
                estado: "DISPONIBLE"
         };
        var i;
        for(i = 0; i<cantidad;i++){
            Restangular.one("articulos", articulo.articuloId).all("items").post(item);
        }
        if(i == cantidad){
            $scope.showSuccess("Se han agregado todos los item al stock", true, 5000);
        }else{
            $scope.showSuccess("Algunos items no se han podido crear "+i+"/"+cantidad, true, 5000);
        }
        $scope.cantidad = 0;
     };

     $scope.init();
 });