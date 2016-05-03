(function() {
    'use strict';

    angular
        .module('asoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('asociacion', {
            parent: 'entity',
            url: '/asociacion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.asociacion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/asociacion/asociacions.html',
                    controller: 'AsociacionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('asociacion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('asociacion-detail', {
            parent: 'entity',
            url: '/asociacion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.asociacion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/asociacion/asociacion-detail.html',
                    controller: 'AsociacionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('asociacion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Asociacion', function($stateParams, Asociacion) {
                    return Asociacion.get({id : $stateParams.id});
                }]
            }
        })
        .state('asociacion.new', {
            parent: 'asociacion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asociacion/asociacion-dialog.html',
                    controller: 'AsociacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                fechaCreacion: null,
                                cuota: null,
                                instrucciones: null,
                                descripcion: null,
                                streetAdress: null,
                                lat: null,
                                lng: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('asociacion', null, { reload: true });
                }, function() {
                    $state.go('asociacion');
                });
            }]
        })
        .state('asociacion.edit', {
            parent: 'asociacion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asociacion/asociacion-dialog.html',
                    controller: 'AsociacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Asociacion', function(Asociacion) {
                            return Asociacion.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('asociacion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('asociacion.delete', {
            parent: 'asociacion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/asociacion/asociacion-delete-dialog.html',
                    controller: 'AsociacionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Asociacion', function(Asociacion) {
                            return Asociacion.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('asociacion', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
