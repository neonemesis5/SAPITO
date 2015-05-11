/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var API_URL = 'http://localhost:8080/SAPITO/ventas/';

/**
 * transporta los datos de la nueva orden de venta hacia el servidor
 * para ser registrados.
 * @type JSON Object
 */
var ordenVentaTransport = {
    clientId: null,
    monto: null,
    montoConCargos: null,
    cargosExtra: [],
    productosEnOrden: []
};

var idProductoActual = null;
var costoTotalOrden = 0;
var cargosTotal = 0;
var granTotal = 0;

function buscarCliente()
{
    var rfc = $('#cliente-rfc').val();

    var reqUrl = API_URL + 'buscarcliente';
    var params = {
        rfc: rfc
    };
    $.get(reqUrl, params, function (data) {
        if (data.empresa) {
            $('#fcliente-empresa').val(data.empresa);
            $('#fcliente-nombre').val(data.nombreContacto);
            ordenVentaTransport.clientId = data.id;
            $('#alert-rfc').addClass('hidden');
        }
        else {
            swal({
                title: "Cliente no encontrado",
                text: "El cliente con rfc: " + rfc + " no existe.",
                type: "error"
            });
        }
    });
}

function buscarProducto()
{
    $('#addp-notfound-alert').addClass('hidden');
    var codigoInventario = $('#addp-cinventario').val();

    var reqUrl = API_URL + 'buscarproducto';
    var params = {
        cinventario: codigoInventario
    };
    $.get(reqUrl, params, function (data) {
        if (data.nombre) {
            $('#addp-nombre').val(data.nombre);
            $('#addp-categoria').val(data.categoria);
            $('#addp-tipo').val(data.tipoProducto);
            $('#addp-ubicacion').val(data.ubicacion);
            $('#addp-cantidaddisp').val(data.cantidad);
            $('#addp-costo').val(data.precioUnitario);
            idProductoActual = data.idinventario;
        }
        else {
            $('#addp-notfound-alert').removeClass('hidden');
            $('#addp-nombre').val('');
            $('#addp-categoria').val('');
            $('#addp-tipo').val('');
            $('#addp-ubicacion').val('');
            $('#addp-cantidaddisp').val('');
            $('#addp-costo').val('');
        }
    });
}

function agregarAOrden()
{
    var trow = '<tr><td>' + $('#addp-nombre').val() + '</td>'
            + '<td>' + $('#addp-cantidad').val() + '</td>'
            + '<td>' + $('#addp-costo').val() + ' </td>';
    $('#tproductos > tbody:last').append(trow);
    ordenVentaTransport.productosEnOrden.push({
        idInventario: idProductoActual,
        cantidad: +$('#addp-cantidad').val()
    });
    $('#alert-productos').addClass('hidden');

    costoTotalOrden = +costoTotalOrden + (+$('#addp-costo').val() * +$('#addp-cantidad').val());
    ordenVentaTransport.monto = costoTotalOrden;
    updateCostos();
    clearAddPForm();
}

function clearAddPForm()
{
    $('#addp-notfound-alert').addClass('hidden');
    $('#addp-nombre').val('');
    $('#addp-categoria').val('');
    $('#addp-tipo').val('');
    $('#addp-ubicacion').val('');
    $('#addp-cantidaddisp').val('');
    $('#addp-costo').val('');

    $('#addp-cinventario').val('');
    $('#addp-cantidad').val('1');
    idProductoActual = null;
}

function agregarCargo()
{
    var concepto = $('#addc-concepto').val();
    var cantidad = $('#addc-cantidad').val();
    if (!concepto || !cantidad) {
        console.log('No concept');
        $('#addc-alert').removeClass('hidden');
    }
    else {
        $('#addc-alert').addClass('hidden');
        var trow = '<tr><td>' + concepto + '</td>'
                + '<td>' + cantidad + '</td>'
                + '<td><button type="button" class="btn btn-success btn-xs">Editar</button></td></tr>';
        $('#tcargos > tbody:last').append(trow);
        ordenVentaTransport.cargosExtra.push({
            concepto: concepto,
            cantidad: cantidad
        });

        cargosTotal = +cargosTotal + +cantidad;
        updateCostos();
        $('#addc-modal').modal('hide');
        clearAddCForm();
    }
}

function cancelarNvaOrdenVenta()
{
    swal({
        title: '¿Seguro que desea cancelar?',
        text: 'Esto eliminará los datos y productos de la orden de venta',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: 'Si, cancelar orden',
        cancelButtonText: 'No',
        closeOnConfirm: true
    }, function (isConfirm) {
        if (isConfirm) {
            location.reload();
        }
    });
}

function clearAddCForm()
{
    $('#addc-alert').addClass('hidden');
    $('#addc-concepto').val('');
    $('#addc-cantidad').val('');
}

function enviarOrden()
{
    if (validateNOForm()) {
        var reqUrl = API_URL + 'nvaorden';
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: reqUrl,
            data: JSON.stringify(ordenVentaTransport),
            success: function (data) {
                ordenRegistrada();
            },
            dataType: 'json'
        });
    }
}

/**
 * Executes this function when OrdenVenta is registered.
 * Confirms registration and Ask to user for generate Factura
 * @returns {undefined}
 */
function ordenRegistrada()
{
    swal({
        title: 'La orden ha sido registrada',
        text: 'Indique si desea generar la factura de la orden de venta',
        type: 'success',
        showCancelButton: true,
        //confirmButtonColor: '#2ECC71',
        confirmButtonText: 'Generar',
        cancelButtonText: 'Ahora no',
        closeOnConfirm: false,
        closeOnCancel: false
    }, function (isConfirm) {
        if (isConfirm) {
            swal({
                title: 'Generada',
                text: 'La factura ha sido generada con exito',
                type: 'success',
                showCancelButton: true,
                //confirmButtonColor: '#2ECC71',
                confirmButtonText: 'Descargar',
                cancelButtonText: 'Descargar luego',
                closeOnConfirm: false,
                closeOnCancel: false
            }, function (isConfirm) {
                swal({
                    title: '¿Imprimir ticket?',
                    text: 'Indique si desea imprimir el ticket de la orden de venta',
                    type: 'info',
                    showCancelButton: true,
                    confirmButtonColor: '#2ECC71',
                    confirmButtonText: 'Imprimir',
                    cancelButtonText: 'No imprimir',
                    closeOnConfirm: false,
                    closeOnCancel: true
                }, function (isConfirm) {
                    if (isConfirm) {
                        swal({
                            title: 'Listo',
                            text: 'El ticket ha sido impreso',
                            type: 'success'
                        }, function (isConfirm) {
                            location.reload();
                        });
                    }
                });
            });
        }
        else {
            swal({
                title: '¿Imprimir ticket?',
                text: 'Indique si desea imprimir el ticket de la orden de venta',
                type: 'info',
                showCancelButton: true,
                confirmButtonColor: '#2ECC71',
                confirmButtonText: 'Imprimir',
                cancelButtonText: 'No imprimir',
                closeOnConfirm: false,
                closeOnCancel: true
            }, function (isConfirm) {
                if (isConfirm) {
                    swal({
                        title: 'Listo',
                        text: 'El ticket ha sido impreso',
                        type: 'success'
                    }, function (isConfirm) {
                        location.reload();
                    });
                }
            });
        }
    });
}

function updateCostos()
{
    ordenVentaTransport.montoConCargos = (+costoTotalOrden + +cargosTotal);
    $('#cargos-total').empty();
    $('#cargos-total').append('Cargo total: $' + cargosTotal);
    $('#costo-totalorden').empty();
    $('#costo-totalorden').append('Total de orden: $' + costoTotalOrden);

    $('#total-orden').empty();
    $('#total-orden').append('Total de la orden: $' + costoTotalOrden);
    $('#total-concargos').empty();
    $('#total-concargos').append('Total con cargos extra: $' + (+costoTotalOrden + +cargosTotal));
    $('#total-final').empty();
    $('#total-final').append('Total final: $' + (+costoTotalOrden + +cargosTotal));
}

function validateNOForm()
{
    var rfc = $('#cliente-rfc').val();
    if (!rfc) {
        $('#alert-rfc').removeClass('hidden');
    }
    if (ordenVentaTransport.productosEnOrden.length < 1) {
        $('#alert-productos').removeClass('hidden');
    }


    return (rfc && ordenVentaTransport.productosEnOrden.length > 0
            && ordenVentaTransport.clientId) ? true : false;
}