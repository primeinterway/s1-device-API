# s1-device-API - Métodos

[![](https://jitpack.io/v/primeinterway/s1-device-API.svg)](https://jitpack.io/#primeinterway/s1-device-API)

	init
		// -7 = Erro de Driver
		// -6 = Manager não conseguiu se comunicar com o dispositivo
		// -5 = USB não identificado (nulo)
		// -4 = Falha de permissão do dispositivo
		// -3 = Manager não configurado
		// -2 = Scanner não encontrado
		// -1 = Falha de comunicação USB
		// 0 = OK
		
	getStatus
		// -2 = Dispositivo não configurado
		// -1 = Falha na comunicação
		// 0 = OK
		
	joinListener
		// -3 = O Listener não pode ser nulo
		// -2 = Dispositivo não configurado
		// -1 = Falha na comunicação
		// 0 = OK
		
	close
		// -2 = Dispositivo não configurado
		// -1 = Falha na comunicação
		// 0 = OK
		
	
		
	init()
		Uso: Inicia e estabelece conexão com o dispositivo USB ideal
		
	close()
		Uso: Encerra conexão com o dispositivo e finaliza o gerenciador
		
	getStatus()
		Uso: Obtém o status da conexão atual com o dispositivo
		
	setSymbology(Boolean, int)
		Uso: Habilita ou desabilita o uso de uma simbologia no scanner
		
	
	
