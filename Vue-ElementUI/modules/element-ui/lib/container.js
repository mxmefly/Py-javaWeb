module.exports =
    /******/ (function (modules) { // webpackBootstrap
    /******/ 	// The module cache
    /******/
    var installedModules = {};
    /******/
    /******/ 	// The require function
    /******/
    function __webpack_require__(moduleId) {
        /******/
        /******/ 		// Check if module is in cache
        /******/
        if (installedModules[moduleId]) {
            /******/
            return installedModules[moduleId].exports;
            /******/
        }
        /******/ 		// Create a new module (and put it into the cache)
        /******/
        var module = installedModules[moduleId] = {
            /******/            i: moduleId,
            /******/            l: false,
            /******/            exports: {}
            /******/
        };
        /******/
        /******/ 		// Execute the module function
        /******/
        modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
        /******/
        /******/ 		// Flag the module as loaded
        /******/
        module.l = true;
        /******/
        /******/ 		// Return the exports of the module
        /******/
        return module.exports;
        /******/
    }

    /******/
    /******/
    /******/ 	// expose the modules object (__webpack_modules__)
    /******/
    __webpack_require__.m = modules;
    /******/
    /******/ 	// expose the module cache
    /******/
    __webpack_require__.c = installedModules;
    /******/
    /******/ 	// define getter function for harmony exports
    /******/
    __webpack_require__.d = function (exports, name, getter) {
        /******/
        if (!__webpack_require__.o(exports, name)) {
            /******/
            Object.defineProperty(exports, name, {
                /******/                configurable: false,
                /******/                enumerable: true,
                /******/                get: getter
                /******/
            });
            /******/
        }
        /******/
    };
    /******/
    /******/ 	// getDefaultExport function for compatibility with non-harmony modules
    /******/
    __webpack_require__.n = function (module) {
        /******/
        var getter = module && module.__esModule ?
            /******/            function getDefault() {
                return module['default'];
            } :
            /******/            function getModuleExports() {
                return module;
            };
        /******/
        __webpack_require__.d(getter, 'a', getter);
        /******/
        return getter;
        /******/
    };
    /******/
    /******/ 	// Object.prototype.hasOwnProperty.call
    /******/
    __webpack_require__.o = function (object, property) {
        return Object.prototype.hasOwnProperty.call(object, property);
    };
    /******/
    /******/ 	// __webpack_public_path__
    /******/
    __webpack_require__.p = "/dist/";
    /******/
    /******/ 	// Load entry module and return exports
    /******/
    return __webpack_require__(__webpack_require__.s = 451);
    /******/
})
/************************************************************************/
/******/({

    /***/ 0:
    /***/ (function (module, exports) {

        /* globals __VUE_SSR_CONTEXT__ */

// IMPORTANT: Do NOT use ES2015 features in this file.
// This module is a runtime utility for cleaner component module output and will
// be included in the final webpack user bundle.

        module.exports = function normalizeComponent(
            rawScriptExports,
            compiledTemplate,
            functionalTemplate,
            injectStyles,
            scopeId,
            moduleIdentifier /* server only */
        ) {
            var esModule
            var scriptExports = rawScriptExports = rawScriptExports || {}

            // ES6 modules interop
            var type = typeof rawScriptExports.default
            if (type === 'object' || type === 'function') {
                esModule = rawScriptExports
                scriptExports = rawScriptExports.default
            }

            // Vue.extend constructor export interop
            var options = typeof scriptExports === 'function'
                ? scriptExports.options
                : scriptExports

            // render functions
            if (compiledTemplate) {
                options.render = compiledTemplate.render
                options.staticRenderFns = compiledTemplate.staticRenderFns
                options._compiled = true
            }

            // functional template
            if (functionalTemplate) {
                options.functional = true
            }

            // scopedId
            if (scopeId) {
                options._scopeId = scopeId
            }

            var hook
            if (moduleIdentifier) { // server build
                hook = function (context) {
                    // 2.3 injection
                    context =
                        context || // cached call
                        (this.$vnode && this.$vnode.ssrContext) || // stateful
                        (this.parent && this.parent.$vnode && this.parent.$vnode.ssrContext) // functional
                    // 2.2 with runInNewContext: true
                    if (!context && typeof __VUE_SSR_CONTEXT__ !== 'undefined') {
                        context = __VUE_SSR_CONTEXT__
                    }
                    // inject component styles
                    if (injectStyles) {
                        injectStyles.call(this, context)
                    }
                    // register component module identifier for async chunk inferrence
                    if (context && context._registeredComponents) {
                        context._registeredComponents.add(moduleIdentifier)
                    }
                }
                // used by ssr in case component is cached and beforeCreate
                // never gets called
                options._ssrRegister = hook
            } else if (injectStyles) {
                hook = injectStyles
            }

            if (hook) {
                var functional = options.functional
                var existing = functional
                    ? options.render
                    : options.beforeCreate

                if (!functional) {
                    // inject component registration as beforeCreate hook
                    options.beforeCreate = existing
                        ? [].concat(existing, hook)
                        : [hook]
                } else {
                    // for template-only hot-reload because in that case the render fn doesn't
                    // go through the normalizer
                    options._injectStyles = hook
                    // register for functioal component in vue file
                    options.render = function renderWithStyleInjection(h, context) {
                        hook.call(context)
                        return existing(h, context)
                    }
                }
            }

            return {
                esModule: esModule,
                exports: scriptExports,
                options: options
            }
        }


        /***/
    }),

    /***/ 451:
    /***/ (function (module, exports, __webpack_require__) {

        module.exports = __webpack_require__(452);


        /***/
    }),

    /***/ 452:
    /***/ (function (module, exports, __webpack_require__) {

        "use strict";


        exports.__esModule = true;

        var _main = __webpack_require__(453);

        var _main2 = _interopRequireDefault(_main);

        function _interopRequireDefault(obj) {
            return obj && obj.__esModule ? obj : {default: obj};
        }

        /* istanbul ignore next */
        _main2.default.install = function (Vue) {
            Vue.component(_main2.default.name, _main2.default);
        };

        exports.default = _main2.default;

        /***/
    }),

    /***/ 453:
    /***/ (function (module, __webpack_exports__, __webpack_require__) {

        "use strict";
        Object.defineProperty(__webpack_exports__, "__esModule", {value: true});
        /* harmony import */
        var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_main_vue__ = __webpack_require__(454);
        /* harmony import */
        var __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_main_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_main_vue__);
        /* harmony import */
        var __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_956d8bb2_hasScoped_false_preserveWhitespace_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_main_vue__ = __webpack_require__(455);
        var normalizeComponent = __webpack_require__(0)
        /* script */

        /* template */

        /* template functional */
        var __vue_template_functional__ = false
        /* styles */
        var __vue_styles__ = null
        /* scopeId */
        var __vue_scopeId__ = null
        /* moduleIdentifier (server only) */
        var __vue_module_identifier__ = null
        var Component = normalizeComponent(
            __WEBPACK_IMPORTED_MODULE_0__babel_loader_node_modules_vue_loader_lib_selector_type_script_index_0_main_vue___default.a,
            __WEBPACK_IMPORTED_MODULE_1__node_modules_vue_loader_lib_template_compiler_index_id_data_v_956d8bb2_hasScoped_false_preserveWhitespace_false_buble_transforms_node_modules_vue_loader_lib_selector_type_template_index_0_main_vue__["a" /* default */],
            __vue_template_functional__,
            __vue_styles__,
            __vue_scopeId__,
            __vue_module_identifier__
        )

        /* harmony default export */
        __webpack_exports__["default"] = (Component.exports);


        /***/
    }),

    /***/ 454:
    /***/ (function (module, exports, __webpack_require__) {

        "use strict";


        exports.__esModule = true;
//
//
//
//
//
//

        exports.default = {
            name: 'ElContainer',

            componentName: 'ElContainer',

            props: {
                direction: String
            },

            computed: {
                isVertical: function isVertical() {
                    if (this.direction === 'vertical') {
                        return true;
                    } else if (this.direction === 'horizontal') {
                        return false;
                    }
                    return this.$slots && this.$slots.default ? this.$slots.default.some(function (vnode) {
                        var tag = vnode.componentOptions && vnode.componentOptions.tag;
                        return tag === 'el-header' || tag === 'el-footer';
                    }) : false;
                }
            }
        };

        /***/
    }),

    /***/ 455:
    /***/ (function (module, __webpack_exports__, __webpack_require__) {

        "use strict";
        var render = function () {
            var _vm = this;
            var _h = _vm.$createElement;
            var _c = _vm._self._c || _h;
            return _c('section', {
                staticClass: "el-container",
                class: {'is-vertical': _vm.isVertical}
            }, [_vm._t("default")], 2)
        }
        var staticRenderFns = []
        var esExports = {render: render, staticRenderFns: staticRenderFns}
        /* harmony default export */
        __webpack_exports__["a"] = (esExports);

        /***/
    })

    /******/
});