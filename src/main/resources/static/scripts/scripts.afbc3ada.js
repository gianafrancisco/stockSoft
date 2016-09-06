"use strict";angular.module("stockApp",["ngAnimate","ngAria","ngCookies","ngMessages","ngResource","ngRoute","ngSanitize","ngTouch","restangular","ui.bootstrap"]).config(["$routeProvider",function(a){a.when("/articulo",{templateUrl:"views/articulo.html",controller:"ArticuloController",controllerAs:"articulo"}).when("/vendedor",{templateUrl:"views/vendedor.html",controller:"VendedorController",controllerAs:"vendedor"}).when("/reserva",{templateUrl:"views/reserva.html",controller:"ReservaController",controllerAs:"reserva"}).when("/ingreso",{templateUrl:"views/ingreso.html",controller:"IngresoController",controllerAs:"ingreso"}).when("/dashboard",{templateUrl:"views/dashboard.html",controller:"DashboardController",controllerAs:"dashboard"}).when("/usuario",{templateUrl:"views/vendedor.html",controller:"VendedorController",controllerAs:"usuario"}).otherwise({redirectTo:"/dashboard"})}]).config(["RestangularProvider",function(a){a.addResponseInterceptor(function(a,b){var c;return"getList"===b&&void 0!=a.content?(c=a.content,c.content=a.content,c.number=a.number,c.totalPages=a.totalPages,c.totalElements=a.totalElements,c.last=a.last,c.size=a.size,c.sort=a.sort,c.first=a.first,c.numberOfElements=a.numberOfElements):c=a,c})}]),angular.module("stockApp").controller("ArticuloController",["$scope","$http","$window","$location","Restangular","$timeout",function(a,b,c,d,e,f){var g=e.all("articulos");a.tipos=[{id:0,label:"Virtual"},{id:1,label:"Físico"}],a.tipo=a.tipos[0],a.cantidad=0,a.listado={numberOfElements:0,number:0,totalElements:0},a.maxSize=100,a.listadoCodigo={},a.articulo={},a.codigo={},a.ipp=50,a.pageNumber=1,a.messageDanger="",a.messageSuccess="",a.dangerShow=!1,a.successShow=!1,a.showDanger=function(b,c,d){a.messageDanger=b,a.dangerShow=c,1==c&&f(a.showDanger,d,!0,"",!1,10)},a.showSuccess=function(b,c,d){a.messageSuccess=b,a.successShow=c,1==c&&f(a.showSuccess,d,!0,"",!1,10)},a.obtenerListaArticulo=function(){var b={page:a.pageNumber-1,size:a.ipp};""!==a.search&&void 0!==a.search&&(b.search=a.search),g.getList(b).then(function(b){a.listado=b,a.pageNumber=a.listado.number+1})},a.buscarArticulo=function(){a.obtenerListaArticulo()},a.agregarArticulo=function(){a.articulo.articuloId=null,a.save(function(){a.articulo={}})},a.modificarArticulo=function(){a.save(function(){a.articulo={}})},a.modificar=function(b){a.articulo=b},a.copiar=function(b){a.modificar(b),a.articulo.articuloId=void 0,a.articulo.codigo="",a.articulo.cantidadStock=0,a.articulo.cantidad=0;var d=c.document.getElementById("codigo");d&&d.focus()},a.save=function(b){null===a.articulo.articuloId?g.post(a.articulo).then(function(){a.obtenerListaArticulo(),void 0!==b&&b()}):a.articulo.put().then(function(){void 0!==b&&b()})},a.eliminar=function(b){confirm("Esta seguro que quiere elimiar el articulo?")&&e.one("articulos",b.articuloId).remove().then(function(){a.obtenerListaArticulo()})},a.init=function(){a.obtenerListaArticulo()},a.isModificable=function(){return null===a.articulo.articuloId||void 0===a.articulo.articuloId},a.cerrarSession=function(){b.post("/logout",{}).success(function(){d.path("/index.html")})},a.agregarItems=function(b,c,d,f){var g,h={tipo:d.id,ordenDeCompra:f,estado:"DISPONIBLE"};for(g=0;c>g;g++)e.one("articulos",b.articuloId).all("items").post(h);g==c?a.showSuccess("Se han agregado todos los item al stock",!0,5e3):a.showSuccess("Algunos items no se han podido crear "+g+"/"+c,!0,5e3),a.cantidad=0},a.init()}]),angular.module("stockApp").controller("VendedorController",["$scope","$http","$window","$location",function(a,b,c,d){a.listado={numberOfElements:0,number:0,totalElements:0},a.maxSize=100,a.vendedor={},a.codigo={},a.ipp=50,a.pageNumber=1,a.obtenerListaVendedor=function(){var c="/vendedores?page="+(a.pageNumber-1);if(""!==a.search&&void 0!==a.search){var d="&search="+a.search;c="/vendedor/search?page="+(a.pageNumber-1)+d}b.get(c).success(function(b){a.listado=b,a.pageNumber=a.listado.number+1})},a.buscar=function(){a.obtenerListaVendedor()},a.agregar=function(){a.vendedor.vendedorId=null,a.save(function(){a.vendedor={}})},a.modificar=function(){a.save(function(){a.vendedor={}})},a.editar=function(b){a.vendedor=b},a.save=function(c){b.put("/vendedor/agregar",a.vendedor).success(function(b){a.vendedor=b,a.obtenerListaVendedor(),void 0!==c&&c()})},a.eliminar=function(c){confirm("Esta seguro que quiere elimiar el vendedor?")&&b.get("/vendedor/delete/"+c.vendedorId).success(function(){a.obtenerListaVendedor(),a.obtenerListaCodigo(c)})},a.init=function(){a.obtenerListaVendedor()},a.isModificable=function(){return void 0===a.vendedor.vendedorId},a.cerrarSession=function(){b.post("/logout",{}).success(function(){d.path("/index.html")})},a.init()}]),angular.module("stockApp").controller("ReservaController",["$scope","$http","$window","$location","Restangular","$timeout","ReservaService",function(a,b,c,d,e,f,g){var h=e.service("articulos"),i=e.service("reservas");a.reserva={descripcion:"",email:"",items:[]},a.listado={numberOfElements:0,number:0,totalElements:0},a.maxSize=100,a.listadoCodigo={},a.articulo={},a.codigo={},a.ipp=50,a.pageNumber=1,a.cantidad=0,a.descripcionError=!1,a.emailError=!1,a.messageSuccess="",a.successShow=!1,a.showSuccess=function(b,c,d){a.messageSuccess=b,a.successShow=c,1==c&&f(a.showSuccess,d,!0,"",!1,10)},a.obtenerListaArticulo=function(){var b={page:a.pageNumber-1,size:a.ipp};""!==a.search&&void 0!==a.search&&(b.search=a.search),h.getList(b).then(function(b){a.listado=b,a.pageNumber=a.listado.number+1})},a.buscarArticulo=function(){a.obtenerListaArticulo()},a.agregarArticulo=function(){a.articulo.articuloId=null,a.save(function(){a.articulo={}})},a.modificar=function(b){a.articulo=b;var d=c.document.getElementById("cantidad");d&&d.focus()},a.init=function(){a.obtenerListaArticulo()},a.isModificable=function(){return null===a.articulo.articuloId||void 0===a.articulo.articuloId},a.reservarItems=function(b,c){e.one("articulos",b.articuloId).getList("items",{page:0,size:c,estado:1}).then(function(a){a.forEach(function(a){a.id=a.itemId,a.estado=1,a.put()})}),a.cantidad=0},a.agregarReserva=function(a,b){var c=!1;void 0===a.items&&(a.items=[]),a.items.forEach(function(a,d){a.articulo.articuloId===b.articuloId&&(a.cantidad++,c=!0)}),c||a.items.push({articulo:b,cantidad:1,sinStock:!1})},a.generarReserva=function(b){if(a.descripcionError=!1,a.emailError=!1,""===b.descripcion||void 0===b.descripcion)return a.descripcionError=!0,!1;if(""===b.email||void 0===b.email)return a.emailError=!0,!1;var c={descripcion:b.descripcion,email:b.email};void 0===b.items&&(b.items=[]);var d=new Promise(function(a,c){b.items.forEach(function(b,d,e){0===b.cantidad&&(b.sinStock=!1);var f={page:0,size:b.cantidad,estado:"DISPONIBLE"};h.one(b.articulo.articuloId).getList("items",f).then(function(f){f.length<b.cantidad&&(b.sinStock=!0,c("La cantidad solicitada del articulo "+b.articulo.codigo+" no se encuentra disponible en stock")),d===e.length-1&&a()})})});d.then(function(){i.post(c).then(function(c){console.log(c),b.items.forEach(function(b,d,e){var f={page:0,size:b.cantidad,estado:"DISPONIBLE"};h.one(b.articulo.articuloId).getList("items",f).then(function(f){f.length<b.cantidad?alert("La cantidad solicitada del articulo "+b.articulo.codigo+" no se encuentra disponible en stock"):f.forEach(function(a){console.log(a),a.reserva=c,a.estado="RESERVADO",a.put()}),d==e.length-1&&(a.showSuccess("La reserva ha sido creada con exito.",!0,5e3),a.reserva.descripcion="",a.reserva.email="",a.reserva.items=[],a.obtenerListaArticulo())})})})})["catch"](function(a){console.log(a),alert(a)})},a.init()}]),angular.module("stockApp").service("ReservaService",function(){var a={};return a.descripcion="",a.email="",a.items=[],a}),angular.module("stockApp").controller("IngresoController",["$scope","$http","$window","$location","Restangular","$timeout",function(a,b,c,d,e,f){var g=e.all("items");a.tipos=[{id:0,label:"Virtual"},{id:1,label:"Físico"}],a.tipo=a.tipos[0],a.cantidad=0,a.listado={},a.maxSize=100,a.ipp=50,a.pageNumber=1,a.messageDanger="",a.messageSuccess="",a.dangerShow=!1,a.successShow=!1,a.showDanger=function(b,c,d){a.messageDanger=b,a.dangerShow=c,1==c&&f(a.showDanger,d,!0,"",!1,10)},a.showSuccess=function(b,c,d){a.messageSuccess=b,a.successShow=c,1==c&&f(a.showSuccess,d,!0,"",!1,10)},a.buscar=function(){var b={page:a.pageNumber-1,size:a.ipp};""!==a.search&&void 0!==a.search&&(b.ordenDeCompra=a.search,g.getList(b).then(function(b){a.listado=b,a.pageNumber=a.listado.number+1}))},a.ingresar=function(a){a.tipo="FISICO",a.put()},a.cancelar=function(a){a.tipo="VIRTUAL",a.put()},a.init=function(){},a.init()}]),angular.module("stockApp").controller("DashboardController",["$scope","Restangular","$timeout",function(a,b,c){var d=b.service("reservas");a.reserva={descripcion:"",email:"",items:[]},a.listado={numberOfElements:0,number:0,totalElements:0},a.maxSize=100,a.listadoCodigo={},a.articulo={},a.codigo={},a.ipp=50,a.pageNumber=1,a.cantidad=0,a.descripcionError=!1,a.emailError=!1,a.messageSuccess="",a.successShow=!1,a.showSuccess=function(b,d,e){a.messageSuccess=b,a.successShow=d,1==d&&c(a.showSuccess,e,!0,"",!1,10)},a.buscar=function(b){var c={page:a.pageNumber-1,sort:"id,desc",size:a.ipp};""!==b&&void 0!==b&&(c.search=b),d.getList(c).then(function(b){a.listado=b,a.pageNumber=a.listado.number+1,a.listado.forEach(function(b,c,d){new Promise(function(a,c){b.getList("items").then(function(c){a({r:b,i:c})})}).then(function(b){b.r.resumen=b.i,b.r.inStock=!0,void 0!=b.r.resumen&&b.r.resumen.forEach(function(a){console.info(a.tipo),"VIRTUAL"==a.tipo&&(b.r.inStock=!1)}),a.$apply()})})})},a.cancelar=function(a){confirm("Quiere cancelar la reserva?")===!0&&(a.estado="CANCELADA",a.put())},a.cerrar=function(a){confirm("Quiere cerrar la reserva?")===!0&&(a.estado="CERRADA",a.put())},a.init=function(){a.buscar("")},a.isModificable=function(){return null===a.articulo.articuloId||void 0===a.articulo.articuloId},a.mostrarAcciones=function(a){return"ACTIVA"===a.estado||"CONFIRMADA"===a.estado},a.mostrarResumenItems=function(a){if(void 0==a)return"";var b=[];a.forEach(function(a,c){var d=-1;b.forEach(function(b,c){return b.articulo.articuloId===a.articulo.articuloId?void(d=c):void 0}),-1===d?b.push({articulo:a.articulo,cant:1}):b[d].cant++});var c="Cantidad - Codigo - Productos\n";return b.forEach(function(a){c+=a.cant+" - "+a.articulo.codigo+" - "+a.articulo.descripcion+"\n"}),c},a.init()}]),angular.module("stockApp").run(["$templateCache",function(a){a.put("views/articulo.html",'<!--\n  ~ Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>\n  ~\n  --> <div class="row"> <div class="col-md-12"> <div class="alert alert-danger" role="alert" ng-show="dangerShow">{{messageDanger}}</div> </div> </div> <div class="row"> <div class="col-md-12"> <div class="alert alert-success" role="alert" ng-show="successShow">{{messageSuccess}}</div> </div> </div> <div class="row"> <div class="col-md-8"> <div id="agregarArticulo" class="panel panel-primary"> <div class="panel-heading">Stock</div> <div class="panel-body"> <form> <div class="form-group-sm"> <label for="descripcion">Art&iacute;culo</label> <input type="text" id="codigo" class="form-control" placeholder="Codigo de barra" ng-model="articulo.codigo"> </div> <div class="form-group-sm"> <label for="descripcion">Descripción</label> <input type="text" id="descripcion" class="form-control" placeholder="Descripcion" ng-model="articulo.descripcion"> </div> <div style="margin-top: 10px"> <button ng-click="agregarArticulo();" class="btn btn-success" ng-disabled="!isModificable();"><span class="glyphicon glyphicon-plus"></span> Agregar nuevo</button> <button ng-click="modificarArticulo();" class="btn btn-primary" ng-disabled="isModificable();"><span class="glyphicon glyphicon-edit"></span> Modificar</button> </div> </form> </div> </div> </div> <div class="col-md-4"> <div id="stock" class="panel panel-primary"> <div class="panel-heading">Agregar Stock</div> <div class="panel-body"> <form> <div class="form-group-sm"> <label for="descripcion">Orden de compra</label> <input type="text" id="ordenDeCompra" class="form-control" ng-model="ordenDeCompra"> </div> <div class="form-group-sm"> <label for="descripcion">Tipo</label> <select class="form-control" ng-options="tipo as tipo.label for tipo in tipos track by tipo.id" ng-model="tipo"></select> </div> <div class="form-group-sm"> <label for="descripcion">Cantidad</label> <input type="number" id="candidad" class="form-control" min="0" max="99999" ng-model="cantidad"> </div> <div style="margin-top: 10px"> <button ng-click="agregarItems(articulo, cantidad, tipo, ordenDeCompra);" class="btn btn-success" ng-disabled="!(!isModificable() && cantidad > 0)"><span class="glyphicon glyphicon-plus"></span> Agregar</button> </div> </form> </div> </div> </div> </div> <div class="row"> <div id="listaArticulos" class="col-md-12"> <div class="panel panel-primary"> <div class="panel-heading">Buscar stock</div> <div class="panel-body"> <div class="form-group-sm"> <label for="buscar">Buscar</label> <input type="text" id="buscar" class="form-control" placeholder="buscar articulo" ng-model="search" ng-change="buscarArticulo();"> </div> <ul uib-pagination total-items="listado.totalElements" ng-model="pageNumber" max-size="maxSize" items-per-page="listado.size" class="pagination-sm" boundary-links="true" ng-change="obtenerListaArticulo();"></ul> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> <div class="table-responsive"> <table id="tablaListaArticulo" class="table"> <thead> <tr> <th>Código</th> <th>Stock (F/V)</th> <th>Descripción</th> <th>Acciones</th> </tr> </thead> <tbody> <tr ng-repeat="articulo in listado"> <td style="width: 20%">{{articulo.codigo}}</td> <td style="width: 10%">{{articulo.stockFisico}} / {{articulo.stockVirtual}}</td> <td style="width: 30%">{{articulo.descripcion}}</td> <td style="width: 20%"> <div class="btn-group hidden-xs"> <button ng-click="modificar(articulo);" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-edit"></span></button> <button ng-click="copiar(articulo);" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-copy"></span></button> <button ng-click="eliminar(articulo);" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-trash"></span></button> </div> <div class="btn-group visible-xs-inline-block"> <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> Accion <span class="caret"></span> </button> <ul class="dropdown-menu"> <li><a ng-click="modificar(articulo);">Modificar</a></li> <li><a ng-click="copiar(articulo);">Copiar</a></li> <li><a ng-click="eliminar(articulo);">Eliminar</a></li> </ul> </div> </td> </tr> </tbody> </table> </div> <div class="panel-footer"> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> </div> </div> </div>'),a.put("views/dashboard.html",'<!--\n  ~ Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>\n  ~\n  --> <div class="row"> <div class="col-md-12"> <div class="alert alert-success" role="alert" ng-show="successShow">{{messageSuccess}}</div> </div> </div> <div class="row"> <div class="col-md-12"> <div class="panel panel-primary"> <div class="panel-heading">Buscar reservas</div> <div class="panel-body"> <div class="form-group-sm"> <label for="buscar">Buscar</label> <input type="text" id="buscar" class="form-control" placeholder="buscar articulo" ng-model="search" ng-change="buscar(search);"> </div> <ul uib-pagination total-items="listado.totalElements" ng-model="pageNumber" max-size="maxSize" items-per-page="listado.size" class="pagination-sm" boundary-links="true" ng-change="buscar(search);"></ul> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> <!--\n            <div class="container-fluid" ng-repeat="reserva in listado">\n                <div class="row"><h4>{{reserva.descripcion}}</h4></div>\n                <div class="row">\n                    <div class="col-md-8">\n                        {{reserva.email}}\n                    </div>\n                    <div class="col-md-4">\n                        <p class="list-group-item-text">\n                            <div class="btn-group">\n                              <button ng-click="" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-plus"></span></button>\n                            </div>\n                        </p>\n                    </div>\n                </div>\n              </a>\n            </div>\n            --> <div class="list-group"> <a class="list-group-item" ng-repeat="reserva in listado"> <div class="row"> <div class="col-md-4 col-sm-4"> <h4 class="list-group-item-heading">{{reserva.descripcion}}</h4> <h5 class="list-group-item-heading">{{reserva.fechaReserva | date:\'short\'}} / {{reserva.fechaCierre | date:\'short\'}}</h5> <h5 class="list-group-item-heading">{{reserva.vendedor}}</h5> <p class="list-group-item-text">{{reserva.email}}</p> </div> <div class="col-md-6 col-sm-6"> <!--\n                        TODO: Add item list detail.\n                    --> <textarea class="form-control" ng-disabled="true">{{mostrarResumenItems(reserva.resumen)}}</textarea> </div> <div class="col-md-2 col-sm-2"> <h6> <span class="label" ng-class="{ \'label-primary\': reserva.estado == \'ACTIVA\', \'label-success\': reserva.estado == \'CERRADA\', \'label-danger\': reserva.estado == \'CANCELADA\', \'label-warning\': reserva.inStock == false }">{{reserva.estado}}</span> </h6> <p class="list-group-item-text"> <div class="btn-group" ng-show="mostrarAcciones(reserva);"> <button ng-click="cerrar(reserva);" class="btn btn-success btn-sm"><span class="glyphicon glyphicon-ok-circle"></span> Cerrar</button> <button ng-click="cancelar(reserva);" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-remove-circle"></span> Cancelar</button> </div> </p> </div> </div> </a> </div> <div class="panel-footer"> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> </div> </div> </div>'),a.put("views/ingreso.html",'<!--\n  ~ Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>\n  ~\n  --> <div class="row"> <div class="col-md-12"> <div class="alert alert-danger" role="alert" ng-show="dangerShow">{{messageDanger}}</div> </div> </div> <div class="row"> <div class="col-md-12"> <div class="alert alert-success" role="alert" ng-show="successShow">{{messageSuccess}}</div> </div> </div> <div class="row"> <div id="listaArticulos" class="col-md-12"> <div class="panel panel-primary"> <div class="panel-heading">Orden de compra</div> <div class="panel-body"> <div class="form-group-sm"> <label for="buscar">Buscar </label> <input type="text" id="buscar" class="form-control" placeholder="buscar stock" ng-model="search" ng-change="buscar();"> </div> <ul uib-pagination total-items="listado.totalElements" ng-model="pageNumber" max-size="maxSize" items-per-page="listado.size" class="pagination-sm" boundary-links="true" ng-change="obtenerLista();"></ul> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> <div class="table-responsive"> <table id="tablaListaArticulo" class="table"> <thead> <tr> <th>Código</th> <th>Descripción</th> <th>Stock</th> <th>Reserva</th> <th>Acciones</th> </tr> </thead> <tbody> <tr ng-repeat="item in listado"> <td style="width: 20%">{{item.articulo.codigo}}</td> <td style="width: 30%">{{item.articulo.descripcion}}</td> <td style="width: 20%">{{item.tipo}}</td> <td style="width: 20%">{{item.reserva.vendedor}}</td> <td style="width: 10%"> <div class="btn-group"> <button ng-click="ingresar(item);" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-log-in"></span></button> <button ng-click="cancelar(item);" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-log-out"></span></button> </div> </td> </tr> </tbody> </table> </div> <div class="panel-footer"> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> </div> </div> </div>'),a.put("views/reserva.html",'<!--\n  ~ Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>\n  ~\n  --> <div class="row"> <div class="col-md-12"> <div class="alert alert-success" role="alert" ng-show="successShow">{{messageSuccess}}</div> </div> </div> <div class="row"> <div class="col-md-12"> <div id="stock" class="panel panel-primary"> <div class="panel-heading">Reserva</div> <div class="panel-body"> <div class="content-fluid"> <div class="row"> <div class="col-md-4"> <div class="form-group-sm" ng-class="{\'has-error\': descripcionError}"> <label for="nombre">Descripción de reserva</label> <input type="text" class="form-control" ng-model="reserva.descripcion"> </div> </div> <div class="col-md-4"> <div class="form-group-sm" ng-class="{\'has-error\': emailError}"> <label for="email">Dirección de email</label> <input type="email" class="form-control" ng-model="reserva.email"> </div> </div> <div class="col-md-4"> <div class="form-group-sm"> <label for="acciones">Acciones</label> <button class="form-control btn btn-success" ng-click="generarReserva(reserva);"><span class="glyphicon glyphicon-plus"></span> Crear Reserva</button> </div> </div> </div> <div class="row"> <div class="table-responsive"> <table id="tablaListaArticulo" class="table"> <thead> <tr> <th>Código</th> <th>Descripción</th> <th>Cantidad</th> <th>Acciones</th> </tr> </thead> <tbody> <tr ng-repeat="item in reserva.items" ng-class="{ \'danger\': item.sinStock }"> <td style="width: 20%">{{item.articulo.codigo}}</td> <td style="width: 30%">{{item.articulo.descripcion}}</td> <td style="width: 30%"><input type="number" ng-model="item.cantidad" min="0" max="{{item.articulo.stockFisico+item.articulo.stockVirtual}}"></td> <td style="width: 20%"></td> </tr> </tbody> </table> </div> </div> </div> </div> </div> </div> </div> <div class="row"> <div class="col-md-12"> <div class="panel panel-primary"> <div class="panel-heading">Buscar artículos</div> <div class="panel-body"> <div class="form-group-sm"> <label for="buscar">Buscar</label> <input type="text" id="buscar" class="form-control" placeholder="buscar articulo" ng-model="search" ng-change="buscarArticulo();"> </div> <ul uib-pagination total-items="listado.totalElements" ng-model="pageNumber" max-size="maxSize" items-per-page="listado.size" class="pagination-sm" boundary-links="true" ng-change="obtenerListaArticulo();"></ul> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> <div class="table-responsive"> <table id="tablaListaArticulo" class="table"> <thead> <tr> <th>Código</th> <th>Descripción</th> <th>Stock (F/V)</th> <th>Acciones</th> </tr> </thead> <tbody> <tr ng-repeat="articulo in listado"> <td style="width: 20%">{{articulo.codigo}}</td> <td style="width: 30%">{{articulo.descripcion}}</td> <td style="width: 10%">{{articulo.stockFisico}} / {{articulo.stockVirtual}}</td> <td style="width: 20%"> <div class="btn-group"> <button ng-click="agregarReserva(reserva, articulo);" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-plus"></span></button> </div> </td> </tr> </tbody> </table> </div> <div class="panel-footer"> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> </div> </div> </div>'),a.put("views/vendedor.html",'<!--\n  ~ Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>\n  ~\n  --> <div class="row"> <div class="col-md-12"> <div id="agregarArticulo" class="panel panel-primary"> <div class="panel-heading">Usuarios</div> <div class="panel-body"> <form> <div class="form-group-sm"> <label for="username">Username</label> <input type="text" id="username" class="form-control" placeholder="Username" ng-model="vendedor.username"> </div> <div class="form-group-sm"> <label for="password">Password</label> <input type="password" id="password" class="form-control" placeholder="Password" ng-model="vendedor.password"> </div> <div class="form-group-sm"> <label for="nombre">Nombre</label> <input type="text" id="nombre" class="form-control" placeholder="Nombre" ng-model="vendedor.nombre" value=""> </div> <div class="form-group-sm"> <label for="apellido">Apellido</label> <input type="text" id="apellido" class="form-control" placeholder="Apellido" ng-model="vendedor.apellido"> </div> <div style="margin-top: 10px"> <button ng-click="agregar();" class="btn btn-success" ng-disabled="!isModificable();"><span class="glyphicon glyphicon-plus"></span>Agregar nuevo</button> <button ng-click="modificar();" class="btn btn-primary" ng-disabled="isModificable();"><span class="glyphicon glyphicon-edit"></span>Modificar</button> </div> </form> </div> </div> <div id="lista"> <div class="panel panel-primary"> <div class="panel-heading">Buscar</div> <div class="panel-body"> <div class="form-group-sm"> <label for="buscar">Buscar</label> <input type="text" id="buscar" class="form-control" placeholder="buscar" ng-model="search" ng-change="buscar();"> </div> <ul uib-pagination total-items="listado.totalElements" ng-model="pageNumber" max-size="maxSize" items-per-page="listado.size" class="pagination-sm" boundary-links="true" ng-change="obtenerListaVendedor();"></ul> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> <table id="tablaListaVendedor" class="table"> <thead> <tr> <th>Username</th> <th>Nombre</th> <th>Apellido</th> <th>Acciones</th> </tr> </thead> <tbody> <tr ng-repeat="vendedor in listado.content"> <td style="width: 10%">{{vendedor.username}}</td> <td style="width: 10%">{{vendedor.nombre}}</td> <td style="width: 10%">{{vendedor.apellido}}</td> <td style="width: 20%"> <div class="btn-group"> <button ng-click="editar(vendedor);" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-edit"></span></button> <button ng-click="eliminar(vendedor);" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-trash"></span></button> </div> </td> </tr> </tbody> </table> <div class="panel-footer"> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> </div> </div> </div> </div>')}]);