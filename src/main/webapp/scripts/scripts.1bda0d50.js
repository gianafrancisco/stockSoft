"use strict";angular.module("stockApp",["ngAnimate","ngAria","ngCookies","ngMessages","ngResource","ngRoute","ngSanitize","ngTouch","restangular"]).config(["$routeProvider",function(a){a.when("/articulo",{templateUrl:"views/articulo.html",controller:"ArticuloController",controllerAs:"articulo"}).when("/vendedor",{templateUrl:"views/vendedor.html",controller:"VendedorController",controllerAs:"vendedor"}).otherwise({redirectTo:"/articulo"})}]).config(["RestangularProvider",function(a){a.addResponseInterceptor(function(a,b){var c;return"getList"===b?(c=a.content,c.content=a.content,c.number=a.number,c.totalPages=a.totalPages,c.totalElements=a.totalElements,c.last=a.last,c.size=a.size,c.sort=a.sort,c.first=a.first,c.numberOfElements=a.numberOfElements):c=a,c})}]),angular.module("stockApp").controller("ArticuloController",["$scope","$http","$window","$location","Restangular",function(a,b,c,d,e){var f=e.all("articulos");a.listado={numberOfElements:0,number:0,totalElements:0},a.maxSize=100,a.listadoCodigo={},a.articulo={},a.codigo={},a.ipp=20,a.pageNumber=1,a.obtenerListaArticulo=function(){var b={page:a.pageNumber-1};""!==a.search&&void 0!==a.search&&(b.search=a.search),f.getList(b).then(function(b){a.listado=b,a.pageNumber=a.listado.number+1})},a.buscarArticulo=function(){a.obtenerListaArticulo()},a.agregarArticulo=function(){a.articulo.articuloId=null,a.save(function(){a.articulo={}})},a.modificarArticulo=function(){a.save(function(){a.articulo={}})},a.modificar=function(b){a.articulo=b},a.copiar=function(b){a.modificar(b),a.articulo.articuloId=void 0,a.articulo.codigo="",a.articulo.cantidadStock=0,a.articulo.cantidad=0;var d=c.document.getElementById("codigo");d&&d.focus()},a.save=function(b){null===a.articulo.articuloId?f.post(a.articulo).then(function(){a.obtenerListaArticulo(),void 0!==b&&b()}):a.articulo.put().then(function(){void 0!==b&&b()})},a.eliminar=function(b){confirm("Esta seguro que quiere elimiar el articulo?")&&e.one("articulos",b.articuloId).remove().then(function(){a.obtenerListaArticulo()})},a.init=function(){a.obtenerListaArticulo()},a.isModificable=function(){return null===a.articulo.articuloId||void 0===a.articulo.articuloId},a.cerrarSession=function(){b.post("/logout",{}).success(function(){d.path("/index.html")})},a.init()}]),angular.module("stockApp").controller("VendedorController",["$scope","$http","$window","$location",function(a,b,c,d){a.listado={numberOfElements:0,number:0,totalElements:0},a.maxSize=100,a.vendedor={},a.codigo={},a.ipp=20,a.pageNumber=1,a.obtenerListaVendedor=function(){var c="/vendedores?page="+(a.pageNumber-1);if(""!==a.search&&void 0!==a.search){var d="&search="+a.search;c="/vendedor/search?page="+(a.pageNumber-1)+d}b.get(c).success(function(b){a.listado=b,a.pageNumber=a.listado.number+1})},a.buscar=function(){a.obtenerListaVendedor()},a.agregar=function(){a.vendedor.vendedorId=null,a.save(function(){a.vendedor={}})},a.modificar=function(){a.save(function(){a.vendedor={}})},a.editar=function(b){a.vendedor=b},a.save=function(c){b.put("/vendedor/agregar",a.vendedor).success(function(b){a.vendedor=b,a.obtenerListaVendedor(),void 0!==c&&c()})},a.eliminar=function(c){confirm("Esta seguro que quiere elimiar el vendedor?")&&b.get("/vendedor/delete/"+c.vendedorId).success(function(){a.obtenerListaVendedor(),a.obtenerListaCodigo(c)})},a.init=function(){a.obtenerListaVendedor()},a.isModificable=function(){return void 0===a.vendedor.vendedorId},a.cerrarSession=function(){b.post("/logout",{}).success(function(){d.path("/index.html")})},a.init()}]),angular.module("stockApp").run(["$templateCache",function(a){a.put("views/articulo.html",'<div> <div id="agregarArticulo" class="panel panel-primary" style="width: 90%; margin: 5%; margin-top: 100px"> <div class="panel-heading">Agregar nuevo artículo</div> <div class="panel-body"> <form> <div class="form-group-sm"> <label for="descripcion">Código de barra</label> <input type="text" id="codigo" class="form-control" placeholder="Codigo de barra" ng-model="articulo.codigo"> </div> <div class="form-group-sm"> <label for="descripcion">Descripción</label> <input type="text" id="descripcion" class="form-control" placeholder="Descripcion" ng-model="articulo.descripcion"> </div> <div class="form-group-sm"> <label for="cantidadStock">Stock</label> <input type="number" id="cantidadStock" class="form-control" placeholder="Stock" ng-model="articulo.cantidadStock" value="" disabled min="0" max="9999"> </div> <div class="form-group-sm"> <label for="cantidad">Cantidad</label> <input type="number" id="cantidad" class="form-control" placeholder="Cantidad" ng-model="articulo.cantidad" min="0" max="9999"> </div> <!--\n                <div class="form-group-sm">\n                    <label for="precio">Precio de lista</label>\n                    <input type="number" step="0.01" id="precio" class="form-control" placeholder="Precio lista"  ng-model="articulo.precioLista" min="0"/>\n                </div>\n--> <div style="margin-top: 10px"> <button ng-click="agregarArticulo();" class="btn btn-success" ng-disabled="!isModificable();"><span class="glyphicon glyphicon-plus"></span>Agregar nuevo</button> <button ng-click="modificarArticulo();" class="btn btn-primary" ng-disabled="isModificable();"><span class="glyphicon glyphicon-edit"></span>Modificar</button> </div> </form> </div> </div> <div id="listaArticulos"> <div class="panel panel-primary" style="width: 90%; margin: 5%"> <div class="panel-heading">Buscar artículos</div> <div class="panel-body"> <div class="form-group-sm"> <label for="buscar">Buscar</label> <input type="text" id="buscar" class="form-control" placeholder="buscar articulo" ng-model="search" ng-change="buscarArticulo();"> </div> <uib-pagination total-items="listado.totalElements" ng-model="pageNumber" max-size="maxSize" items-per-page="listado.size" class="pagination-sm" boundary-links="true" ng-change="obtenerListaArticulo();"></uib-pagination> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> <table id="tablaListaArticulo" class="table"> <thead> <tr> <th>Código</th> <!--<th>Precio / Lista</th>--> <th>Stock</th> <th>Descripción</th> <th>Acciones</th> </tr> </thead> <tbody> <tr ng-repeat="articulo in listado"> <td style="width: 20%">{{articulo.codigo}}</td> <!--<td style="width: 10%">{{articulo.precio | currency}} / {{articulo.precioLista | currency}}</td>--> <td style="width: 10%">{{articulo.cantidadStock}}</td> <td style="width: 30%">{{articulo.descripcion}}</td> <td style="width: 20%"> <div class="btn-group"> <!--\n                            <button ng-click="modificar(articulo);" class="btn btn-primary btn-sm">Modificar</button>\n                            <button ng-click="copiar(articulo);" class="btn btn-primary btn-sm">Copiar</button>\n                            <button ng-click="eliminar(articulo);" class="btn btn-danger btn-sm">Eliminar</button>\n                          --> <button ng-click="modificar(articulo);" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-edit"></span></button> <button ng-click="copiar(articulo);" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-copy"></span></button> <button ng-click="eliminar(articulo);" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-trash"></span></button> </div> </td> </tr> </tbody> </table> <div class="panel-footer"> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> </div> </div> </div>'),a.put("views/vendedor.html",'<div> <div id="agregarArticulo" class="panel panel-primary" style="width: 90%; margin: 5%; margin-top: 100px"> <div class="panel-heading">Usuarios</div> <div class="panel-body"> <form> <div class="form-group-sm"> <label for="username">Username</label> <input type="text" id="username" class="form-control" placeholder="Username" ng-model="vendedor.username"> </div> <div class="form-group-sm"> <label for="password">Password</label> <input type="password" id="password" class="form-control" placeholder="Password" ng-model="vendedor.password"> </div> <div class="form-group-sm"> <label for="nombre">Nombre</label> <input type="text" id="nombre" class="form-control" placeholder="Nombre" ng-model="vendedor.nombre" value=""> </div> <div class="form-group-sm"> <label for="apellido">Apellido</label> <input type="text" id="apellido" class="form-control" placeholder="Apellido" ng-model="vendedor.apellido"> </div> <div style="margin-top: 10px"> <button ng-click="agregar();" class="btn btn-success" ng-disabled="!isModificable();"><span class="glyphicon glyphicon-plus">Agregar nuevo</span></button> <button ng-click="modificar();" class="btn btn-primary" ng-disabled="isModificable();"><span class="glyphicon glyphicon-edit">Modificar</span></button> </div> </form> </div> </div> <div id="lista"> <div class="panel panel-primary" style="width: 90%; margin: 5%"> <div class="panel-heading">Buscar</div> <div class="panel-body"> <div class="form-group-sm"> <label for="buscar">Buscar</label> <input type="text" id="buscar" class="form-control" placeholder="buscar" ng-model="search" ng-change="buscar();"> </div> <uib-pagination total-items="listado.totalElements" ng-model="pageNumber" max-size="maxSize" items-per-page="listado.size" class="pagination-sm" boundary-links="true" ng-change="obtenerListaVendedor();"></uib-pagination> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> <table id="tablaListaVendedor" class="table"> <thead> <tr> <th>Username</th> <th>Nombre</th> <th>Apellido</th> <th>Acciones</th> </tr> </thead> <tbody> <tr ng-repeat="vendedor in listado.content"> <td style="width: 10%">{{vendedor.username}}</td> <td style="width: 10%">{{vendedor.nombre}}</td> <td style="width: 10%">{{vendedor.apellido}}</td> <td style="width: 20%"> <div class="btn-group"> <button ng-click="editar(vendedor);" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-edit"></span></button> <button ng-click="eliminar(vendedor);" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-trash"></span></button> </div> </td> </tr> </tbody> </table> <div class="panel-footer"> <pre>Página: {{pageNumber}} / {{listado.totalPages}}</pre> </div> </div> </div> </div>')}]);