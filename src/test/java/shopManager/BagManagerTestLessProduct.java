package shopManager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import shopmanager.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mockitoSession;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;

import exceptions.NoEnoughStock;
import exceptions.NotInStock;
import exceptions.UnknownRepo;
import model.Product;
import model.Order;
import persistency.OrderRepository;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * @author Daniel Neira
 * Clase para realizar los test de Less Product a la clase MyBagManager, o a cualquier otra clase que implemente BagManager siempre que se sustituya la declaración private static MyBagManager micestaTesteada;
 *
 */
@ExtendWith(MockitoExtension.class)
class BagManagerTestLessProduct {

private static Logger trazador=Logger.getLogger(ProductTest.class.getName());
	
	//Creo los objetos sustitutos (representantes o mocks)
	//Son objetos contenidos en MyBagManager de los que aún no disponemos el código
	@Mock(serializable = true)
	private static Product producto1Mock= Mockito.mock(Product.class);
	@Mock
	private static StockManager stockMock= Mockito.mock(StockManager.class);
	@Mock 
	private static OrderRepository repositoryMock= Mockito.mock(OrderRepository.class);
	@Mock
	private static Order orderMock=Mockito.mock(Order.class);
	
	//Inyección de dependencias
	//Los objetos contenidos en micestaTesteada son reemplazados automáticamente por los sustitutos (mocks)
	@InjectMocks
	private static MyBagManager micestaTesteada;

	
	//Servirán para conocer el argumento con el que se ha invocado algún método de alguno de los mocks (sustitutos o representantes)
	//ArgumentCaptor es un genérico, indico al declararlo el tipo del argumento que quiero capturar
	@Captor
	private ArgumentCaptor<Integer> intCaptor;
	@Captor
	private ArgumentCaptor<Product> productCaptor;


	/**
	 * @see BeforeEach {@link org.junit.jupiter.api.BeforeEach}
	 */
	
	@BeforeEach
	void setUpBeforeClass(){
		//Todos los tests empiezan con la bolsa vacía
		
		   micestaTesteada.reset();
	}
	
	/**
	 * Test method for {@link shopmanager.BagManager#lessProduct(model.Product)}.
	 * @throws NotInStock lanza cualquier excepción de sus clientes, no las gestiona siempre internamente
	 */
	@Test
	@Tag("unidad")
	@DisplayName("Prueba del método que elimina un producto")
	void testLessProduct() throws NotInStock {
		
		Mockito.when(producto1Mock.getId()).thenReturn("id1");
		Mockito.when(producto1Mock.getNumber()).thenReturn(1);
	
		/* Intentamos eliminar un producto que no existe, debería lanzar la excepción NoInStock */
		try {
			micestaTesteada.lessProduct(producto1Mock);
	    	/* Debe saltar la excepción así que no debe llegar aquí */
	    	fail("No salta la excepción del stock");
		}
		catch(NotInStock e) {
			/* Si es la excepcion que esperamos, la capturamos y proseguimos con el test */
			trazador.info("Si el producto no existe no podemos eliminarlo. OK");
			assertEquals("El producto no existe, no podemos eliminarlo",e.getMessage(),"El mensaje de la excepción no es correcto");
		}
		
		/* Necesitamos añadir un producto para comprobar que se elimina correctamente */
		try {
			/* No debería haber problemas, pero lo hacemos en un try-catch para controlar mejor las excepciones */
			micestaTesteada.addProduct(producto1Mock);
			
			/* Nos aseguramos de que el producto está añadido correctamente */
			assertFalse(micestaTesteada.findProduct("id1").isEmpty());
			assertEquals(1,micestaTesteada.findProduct("id1").get().getNumber(),"El producto insertado debía tener una unidad");

		}
		catch(NoEnoughStock e) {
			/* Si es la excepcion que esperamos, la capturamos y proseguimos con el test */
			trazador.info("No se ha podido añadir el producto para después eliminarlo. ERROR");
	    	fail("No se ha podido añadir el producto para después eliminarlo");

		}
		
		/* Una vez añadido si podemos eliminarlo */
		try {
			micestaTesteada.lessProduct(producto1Mock);
	    	
		}
		catch(NotInStock e) {
			
			/* No debe saltar la excepción así que no debería llegar aquí */
	    	fail("Salta la excepción del stock que no debería. ERROR");
			
		}
				
	    //Aseguro que se ha eliminado de la cesta
	    assertTrue(micestaTesteada.findProduct("id1").isEmpty(),"La cesta no está vacía");
	    assertFalse(micestaTesteada.findProduct("id1").isPresent(),"La cesta no está vacía");
	    
	}

}
