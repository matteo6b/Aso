(function() {
    'use strict';

    angular
        .module('asoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inscripcion-evento', {
            parent: 'entity',
            url: '/inscripcion-evento',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.inscripcionEvento.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inscripcion-evento/inscripcion-eventos.html',
                    controller: 'InscripcionEventoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscripcionEvento');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('inscripcion-evento-detail', {
            parent: 'entity',
            url: '/inscripcion-evento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'asoApp.inscripcionEvento.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inscripcion-evento/inscripcion-evento-detail.html',
                    controller: 'InscripcionEventoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscripcionEvento');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InscripcionEvento', function($stateParams, InscripcionEvento) {
                    return InscripcionEvento.get({id : $stateParams.id});
                }]
            }
        })
        .state('inscripcion-evento.new', {
            parent: 'inscripcion-evento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscripcion-evento/inscripcion-evento-dialog.html',
                    controller: 'InscripcionEventoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entrada: null,
                                fechains: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('inscripcion-evento', null, { reload: true });
                }, function() {
                    $state.go('inscripcion-evento');
                });
            }]
        })
        .state('inscripcion-evento.edit', {
            parent: 'inscripcion-evento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscripcion-evento/inscripcion-evento-dialog.html',
                    controller: 'InscripcionEventoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InscripcionEvento', function(InscripcionEvento) {
                            return InscripcionEvento.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscripcion-evento', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inscripcion-evento.delete', {
            parent: 'inscripcion-evento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscripcion-evento/inscripcion-evento-delete-dialog.html',
                    controller: 'InscripcionEventoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InscripcionEvento', function(InscripcionEvento) {
                            return InscripcionEvento.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscripcion-evento', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
