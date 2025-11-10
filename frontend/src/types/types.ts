export type Evento = {
    fechaHoraOcurrencia: string,
    latitudEpicentro: number,
    latitudHipocentro: number,
    longitudEpicentro: number,
    longitudHipocentro: number
    valorMagnitud: number;
}

export type DetalleProm =  {
    eventoSismico: {
        fechaHoraOcurrencia: string;
        latitudEpicentro: number;
        longitudEpicentro: number;
        latitudHipocentro: number;
        longitudHipocentro: number;
        valorMagnitud: number;
    };
    datosSismicos: {
        alcanceSismo: string;
        clasificacionSismo: string;
        origenDeGeneracion: string;
    };
    seriesTemporales: {
        codigoEstacion: number;
        muestrasSismicas: {
            fechaHoraMuestra: string;
            detalles: {
                valor: string;
                tipoDeDato: {
                    denominacion: string;
                    nombreUnidadMedida: string;
                };
            }[];
        }[];
    }[];
    cambiosDeEstado: {
        fechaHoraDesde: string;
        fechaHoraHasta: string | null;
        responsableInspeccion: string | null;
        estado: string;
    }[];
}




/* 
export type DetalleProm = {
    detalle: Detalle
}


export type Detalle = {
    eventoSismicos: Evento,
    datosSismicos: DatosSimicos,
    serieTemporal:SerieTemporal[] 
    cambioEstado: CambioEstado[]    
}


export type DatosSimicos = {
    alcanceSismo: String,
    clasificacionSismo: String,
    origenDeGeneracion: String
}

export type SerieTemporal = {
    codigoEstacion: number,
    muestraSismica: MuestraSismica[]
}

type MuestraSismica = {
    fechaHoraMuestra: String, 
    detalleMuestraSismica: DetalleMuestraSismica[]
}

type DetalleMuestraSismica = {
    valor: String,
    tipoDeDato: TipoDeDato

}

type TipoDeDato = {
    denominacion: String,
    nombreUnidadMedida: String
}


export type CambioEstado = {
    fechaHoraDesde: String,
    fechaHoraHasta: String,
    responsableInspeccion: String,
    estado: String
}

*/