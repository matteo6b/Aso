(function() {
    'use strict';

    angular
        .module('asoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('registerSocio', {
            parent: 'account',
            url: '/registerSocio',
            data: {
                authorities: [],
                pageTitle: 'registerSocio.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/register/registerSocio.html',
                    controller: 'RegisterSocioController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('register');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
