(function() {
    'use strict';

    angular
        .module('asoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('evento', {
            parent: 'entity',
            url: '/evento',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.evento.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/evento/eventos.html',
                    controller: 'EventoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('evento');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('evento-detail', {
            parent: 'entity',
            url: '/evento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.evento.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/evento/evento-detail.html',
                    controller: 'EventoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('evento');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Evento', function($stateParams, Evento) {
                    return Evento.get({id : $stateParams.id});
                }]
            }
        })
        .state('evento.new', {
            parent: 'evento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evento/evento-dialog.html',
                    controller: 'EventoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                fecha: null,
                                streetAdress: null,
                                lat: null,
                                lng: null,
                                capacidad: null,
                                entrada: null,
                                descripcion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('evento', null, { reload: true });
                }, function() {
                    $state.go('evento');
                });
            }]
        })
        .state('evento.edit', {
            parent: 'evento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evento/evento-dialog.html',
                    controller: 'EventoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Evento', function(Evento) {
                            return Evento.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('evento', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('evento.delete', {
            parent: 'evento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/evento/evento-delete-dialog.html',
                    controller: 'EventoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Evento', function(Evento) {
                            return Evento.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('evento', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
