angular.module('test',['ngResource', 'ngRoute','ngSanitize'])
.config(function($locationProvider, $routeProvider,log4jProvider ){

	    log4jProvider.enableDebug(true);

        $routeProvider.when("/document", {
            templateUrl : "vue/document.html",
            controller : "childController"
        })
        .when("/administration", {templateUrl : "vue/administration.html"})
        .when("/administration/:tab", {templateUrl : "vue/administration.html"})
        .when("/connection", {templateUrl : "vue/connection.html"})
        .when("/inscription", {templateUrl : "vue/inscription.html"})
        .otherwise({
                templateUrl : "vue/index.html",
                controller : "childController",
                //resolve :  {
                //    nbre : function(){
                //        return 12;
                //    }
                //}
        });

        //$locationProvider.html5Mode(true);
    })

//#######################################################################################
//		CONTROLLER
//#######################################################################################

.controller('parentController', function($scope, $http, $location, $routeParams, $route){
	$scope.page = 1;
	$scope.nbpage = 6;
    $scope.sens = false;
    $scope.order = "name";

    var path = $location.path().split("/");
    var currentMenuItem = (path.length == 1)? "acceuil" : path[1] ;
    $scope.menu = [
        {name:"Acceuil"},
        {name:"Document", location:"#/document"},
        {name:"Administration", location:"#/administration"}
    ];
    $scope.type = {
        'application/pdf' : 'pdf',
        'application/msword':'word',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document':'word',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet':'excel',
        'application/vnd.ms-excel':'excel csv',
        'application/vnd.ms-powerpoint':'powerpiont',
        'application/octet-stream':'archive',
        'text/plain':'text',
        'video/mp4':'video',
        'audio/mp3':'audio',
        'image/png':'image png',
        'image/jpeg':'image jpg',
        'image/gif':'image gif',
    }
    $scope.users = [];
    $scope.roles = [];
        
    $scope.documents = [];
    $scope.categories = [];
    $scope.groupes = [];

    $scope.getPage = function(p){
		$scope.page = p;
	}
	$scope.getPageClass = function(p){
		return $scope.page == p ? "active" : "";
	}
    $scope.getMenuItemClass = function (item) {
        return (currentMenuItem == angular.lowercase(item.name) || item == null) ? "active" : "";
    }
    $scope.selectMenuItem = function (item) {
        currentMenuItem = angular.lowercase(item.name);
    }
    $scope.triByField = function (item) {
        $scope.sens = !$scope.sens;
        $scope.order = item;
    }

    $scope.tab = 1;

    $scope.$on("$routeChangeSuccess", function () {
        if($location.path().indexOf("/administration/") == 0){
            $scope.tab = $routeParams.tab;
            currentMenuItem = "administration";
        }else $scope.tab = 1;
    })

})
.controller('childController',function($scope, $controller, $http, log4j, $location, $timeout){

    $controller('parentController', {$scope: $scope});

    $scope.addDocNotif = $scope.categorieNotif = $scope.groupeNotif = $scope.inscriptionNotif= -1;

    // GESTION DOCUMENTS.
    $scope.getAllDocuments = function () {
        $http.get("http://localhost:8080/document")
            .success(function (data) {
                $scope.documents = data;

            })
            .error(function (error) {
                console.log(error);
            })
    }
    $scope.saveDocument = function () {
        var fd = new FormData();
        angular.forEach($scope.files, function(file){
            fd.append('files', file);
        });
        $scope.docInfo.cles = $scope.docInfo.cles.split(",");

        fd.append('name',$scope.docInfo.name);
        fd.append('description',$scope.docInfo.description);
        fd.append('categorie',$scope.docInfo.categorie);
        fd.append('group',$scope.docInfo.group);
        fd.append('cles',$scope.docInfo.cles);

        console.log($scope.docInfo, $scope.files);
        $scope.addDocNotif = 0;
        $http.post("http://localhost:8080/upload",fd,
            {
                transformResquest : angular.identity,
                headers:{'content-type': undefined }
            }
        )
            .success(function(d,s){
                $scope.addDocNotif = 1;
                $scope.documents.push(d);
                $location.path("/document");
                $scope.docInfo = null;
                $timeout(function () {
                    $scope.addDocNotif = -1;
                },2000);
            })
            .error(function(e){ console.log(e);$scope.addDocNotif = 2;});
    }
    $scope.downloadDocument= function (file ) {

        $http.get( "http://localhost:8080/download/"+file.id,{ responseType: 'arraybuffer' })
            .success(function (d) {
                //console.log(d)
                var blob = new Blob([d], {type:file.type});
                var url = window.URL.createObjectURL(blob);
                var anchor = document.createElement("a");
                anchor.download = file.name;
                anchor.href = url;
                anchor.click();
            })
            .error(function (e) {
                console.log(e)
            })
    }
    $scope.deleteDocument = function (file) {
        $http.delete("http://localhost:8080/document/"+file.id)
            .success(function (d,s) {
                var index = $scope.documents.indexOf(file);
                $scope.documents.splice(index,1);
                console.log(s)
            })
            .error(function (e) {
                console.log(e)
            })
    }

    // GESTION GROUPES.
    $scope.getAllGroupes = function () {
        $http.get("http://localhost:8080/groupe/")
            .success(function (d) {
                $scope.groupes = d;
            })
            .error(function (e) {
                console.log(e)
            })
    }
    $scope.addGroupe = function (groupe) {
        $scope.groupeNotif = 0;
        $http.post("http://localhost:8080/groupe/",groupe)
            .success(function (d) {
                $scope.groupeNotif = 1;
                console.log(d);
                $scope.groupes.push(d);
            })
            .error(function (e) {
                $scope.groupeNotif = 2;
                console.log(e)
            })
    }
    $scope.deleteGroupe = function (groupe) {
        $http.delete("http://localhost:8080/groupe/"+groupe.id)
            .success(function (d) {
                var index = $scope.groupes.indexOf(groupe);
                $scope.groupes.splice(index,1);
            })
            .error(function (e) {
                console.log(e)
            })
    }

    // GESTION CATEGORIES.
    $scope.getAllCategories = function () {
            $http.get("http://localhost:8080/categorie/")
                .success(function (d) {
                    $scope.categories = d;
                })
                .error(function (e) {
                    console.log(e)
                })
        }
    $scope.addCategorie = function (categorie) {
        $scope.categorieNotif = 0;
            $http.post("http://localhost:8080/categorie/",categorie)
                .success(function (d) {
                    $scope.categorieNotif = 1;

                    console.log(d);
                    $scope.categories.push(d);
                })
                .error(function (e) {
                    $scope.categorieNotif = 2;
                    console.log(e)
                })
        }
    $scope.deleteCategorie = function (categorie) {
            $http.delete("http://localhost:8080/categorie/"+categorie.id)
                .success(function (d) {
                    var index = $scope.categories.indexOf(categorie);
                    $scope.categories.splice(index,1);
                })
                .error(function (e) {
                    console.log(e)
                })
        }

    //GESTION USER.
    $scope.getAllUsers = function () {
            $http.get("http://localhost:8080/user/")
                .success(function (d) {
                    $scope.users = d;
                })
                .error(function (e) {
                    console.log(e)
                })
        };
    $scope.deleteUser = function (user) {
        $http.delete("http://localhost:8080/user/"+user.id).success(function (d) {
            console.log(d)
            $scope.users.splice($scope.users.indexOf(user),1)
        }).error(function(e){ console.log(e)})
    };
    $scope.approveUser = function (user) {
        user.active = !user.active;
        $http.put("http://localhost:8080/user/"+user.id, user).success(function (d) {
            console.log(d)
        }).error(function(e){ console.log(e)})
    };
    $scope.addUser = function (user, param){
        var fd = new FormData();
        fd.append('user', angular.toJson(user));
        fd.append('groupes', param.groupes);
        fd.append('roles', param.roles);

        $http.post("http://localhost:8080/user/",fd,{
            transformResquest : angular.identity,
            headers:{'content-type': undefined }
        }).success(function (d) {
            console.log(d)
            $scope.users.push(d);
        }).error(function(e){ console.log(e)})
    };
    $scope.updateUserRole = function (idUser, role) {

    }
    $scope.updateUserGroup = function (idUser, group) {

    }

    //GESTION ROLES.
    $scope.getAllRoles = function () {
        $http.get("http://localhost:8080/role/")
            .success(function (d) {
                $scope.roles = d;
            })
            .error(function (e) {
                console.log(e)
            })
    };
    $scope.addRole = function (role) {
            console.log(role)
            $scope.roleNotif = 0;
            $http.post("http://localhost:8080/role/",role)
                .success(function (d) {
                    $scope.roleNotif = 1;

                    console.log(d);
                    $scope.roles.push(d);
                })
                .error(function (e) {
                    $scope.roleNotif = 2;
                    console.log(e)
                })
        }
    $scope.deleteRole = function (role) {
            $http.delete("http://localhost:8080/role/"+role.id)
                .success(function (d) {
                    var index = $scope.roles.indexOf(role);
                    $scope.roles.splice(index,1);
                })
                .error(function (e) {
                    console.log(e)
                })
        }


    //INIT DATA.
    $scope.getAllDocuments();
    $scope.getAllGroupes();
    $scope.getAllCategories();
    $scope.getAllRoles();
    $scope.getAllUsers();

    $scope.inField = function (item) {
        return (  angular.lowercase(item.name).search(angular.lowercase($scope.mot)) > -1
               || angular.lowercase(item.description).search(angular.lowercase($scope.mot)) > -1)
               ? true : false ;
    }

    })
.controller('testController', function($scope, $http){
    $scope.butona = "afficher table user";
    $scope.text = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium animi beatae debitis " +
            "dolore est exercitationem illum labore minima nisi officia quia quo quos repellat sed, vitae? Illo non " +
            "suscipit voluptatum.";

    $scope.i = true;
    $scope.open = function(i){
        $scope.i = !$scope.i;
    }

    $scope.users =[];
    $http.get("http://localhost:8080/user")
        .success(function (result) {
            $scope.users = result;
            console.log(result);
        })
        .error(function (e) {
            console.log(e);
        })



})
    .directive('panel', function () {

        return {
            restrict : 'E',
            scope:false,
            template : '<div class="well"> name : <input type="text" ng-model="data.name"/> ' +
                       '<br/>city : <input type="text" ng-model="city"/>' +
                       '<br/>country : <input type="text" ng-model="country"/>' +
                       '<br/> {{ data | json}}, {{ city}}, {{ country}} ' +
                       '</div>',
            link : function (scope, element, attrs) {}
        }
    })

//#######################################################################################
//		DIRECTIVE
//#######################################################################################

.directive('ngFile', function($parse){
	return {
		restrict : 'A',
		link : function(scope, element, attrs){
			element.bind('change', function(){
				scope.$apply(function(){
					$parse(attrs['ngFile']).assign(scope, element[0].files);
				})
			});

		}
	}
})
.directive('plus', function(){
	return {
		restrict : 'E',
		scope:{
			value : "="
		},
		link :  function(scope, element, attrs){
			var btn = angular.element("<button>").text("+");
			btn.addClass('btn btn-primary btn-xs');
			element.append(btn);
			btn.on('click', function(){
				scope.$apply(function(){
					scope.value++;
				});
			});
		}
	}
})
.directive('foreach', function(){
	return{
		scope : {
			data : "=",
			item : "@"
		},
		transclude: 'element',
		compile : function(element, attrs, fnct){
			return function($scope, $element, $attr){
				$scope.$watch('data.length', function(){
					var parent =  $element.parent();
					parent.children().remove();
					for (var i = 0; i < $scope.data.length; i++) {
						var child = $scope.$new();
						child[$scope.item] = $scope.data[i];
						fnct(child, function(clone){ parent.append(clone); });
					};
				})
			}
		}
	}
})
.directive('tab', function(){
	return{
		transclude : true,
		scope : {
			total:"=total",
			produits:"=produits"
		},
		controller : function($scope, $element, $attrs){
			this.updateTotal = function(){
				var n = 0;
				for (var i = 0; i < $scope.produits.length; i++) {
					n += Number($scope.produits[i].quantity);
				}
				$scope.total = n;
			}
		}
	}
})
.directive('line', function(){
	return{
		require : '^tab',
		template : function(){
			return angular.element(document.querySelector("#template4")).html();
		},
		link : function(scope, element, attr, ctrl){
			scope.$watch('i.quantity', function(){
				ctrl.updateTotal();
			});
		}
	}
})
.directive('evalue',function($compile){
	return {
		template : function(){
			return angular.element(document.querySelector("#compile")).html();
		},
		link : function(scope, element, attrs){
			var text = element.find('input');
			var btn = element.find('button');
			btn.on('click', function(event) {
				var cnt = scope.cd;
				var el = angular.element(cnt);
				var fnct = $compile(el);
				fnct(scope);
				element.parent().append(el);
			});
		}

	}
})
.directive('liason', function(){
	return {
		template : '<div class=""><input type="text" ng-model="local" /></div>',
		scope :{
			local : "=",
			fnct  : "&"
		},
		link : function(scope, element, attrs){
			scope.$watch('local',function(n){
				scope.fnct();
			});
		}
	}
})

//#######################################################################################
//		SERVICE
//#######################################################################################

.provider('log4j', function(){
	var debug = true;
	return{
		enableDebug : function(d){
			if(angular.isDefined(d)){  return debug = d;}
			else return debug;
		},
		$get : function(){
			return{
				count : 0,
				log : function(msg){
					if(debug) console.log('LOG ['+ (this.count++) +'] : ',msg,' at '+ (new Date().toTimeString().split(" ")[0]));
					else console.log("CHANGER LE DEBUG MODE EN TRUE.")
				}
			}
		}
	}
})

// .provider('bd',function($resource){
// 	var url = 'http://localhost/posts';
// 	return{
// 		setUrl : function(val){
// 			if(angular.isString(val)){
// 				return url = val;
// 			}else{
// 				return url;
// 			}
// 		},
// 		$get: function(){
// 			console.log(url);
// 			return $resource(url, {id : '@id'}, {
// 				get : {method:'GET'},
// 				save : {method:'POST'},
// 				update : {method:'PUT'},
// 				delete : {method:'DELETE'},
// 			})
// 		}
// 	}
// })

//#######################################################################################
//		FILTER
//#######################################################################################

.filter("pages",function(){
	return function(input, nb){
		if(angular.isArray(input)){
			var output =[];
			for (var i = 0; i < Math.ceil(input.length/nb); i++) {
				output.push(i);
			}
			return output;
		}else return input;
	}
})
.filter('lettre',function(){
	return function(input){
		return ( (input / 10) < 1 ) ? '0'+input : input;
	}
})
.filter("pagination",function($filter){
	return function(input,i,nb){
		if(angular.isArray(input) && angular.isNumber(i) && angular.isNumber(nb)){
			var j = (i-1)*nb;
			if(input.length < j) return [];
			else{
				return input.splice(j, nb);
			}
		}else return input;
	}
})
.filter('blob',function(){
	return function(input){
		return (window.URL).createObjectURL(input);
	}
})
.filter('Mo',function($filter){
	return function(input){
        if(input < 1000) {
            div = 1;
            unit = " Octet";
        }
        else if(input > 100000) {
            div = (1024 * 1024);
            unit = " Mo";
        }
        else {
            div = 1024;
            unit = " Ko";
        }
		return $filter("number")(input / div, 2)+unit;
	}
})
.filter('hilight', function () {
    return function (input, mot) {
       if (mot) {
           return angular.lowercase(input).replace(mot, "<span class='hilight'>"+mot+"</span>");
       }
       return input;

    }
 })
