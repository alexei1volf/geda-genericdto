
/*
 * This code is distributed under The GNU Lesser General Public License (LGPLv3)
 * Please visit GNU site for LGPLv3 http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright Denis Pavlov 2009
 * Web: http://www.genericdtoassembler.org
 * SVN: https://svn.code.sf.net/p/geda-genericdto/code/trunk/
 * SVN (mirror): http://geda-genericdto.googlecode.com/svn/trunk/
 */

package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.adapter.BeanFactory;
import com.inspiresoftware.lib.dto.geda.assembler.examples.collections.*;
import com.inspiresoftware.lib.dto.geda.assembler.examples.maps.*;
import com.inspiresoftware.lib.dto.geda.assembler.examples.simple.TestDto15Class;
import com.inspiresoftware.lib.dto.geda.assembler.examples.simple.TestEntity15Class;
import com.inspiresoftware.lib.dto.geda.exception.*;
import com.inspiresoftware.lib.dto.geda.utils.ParameterizedSynthesizer;
import com.inspiresoftware.lib.dto.geda.utils.ParameterizedSynthesizer.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.*;


/**
 * DTOAssembler test.
 *
 * @author Denis Pavlov
 * @since 1.0.0
 *
 */
@RunWith(value = ParameterizedSynthesizer.class)
public class DTOAssemblerDtoMapTest {

	private static final int I_3 = 3;
	
	private String synthesizer;
	
	/**
	 * @param synthesizer parameter
	 */
	public DTOAssemblerDtoMapTest(final String synthesizer) {
		super();
		this.synthesizer = synthesizer;
	}

	/**
	 * @return synthesizers keys
	 */
	@Parameters
	public static Collection<String[]> data() {
		final List<String[]> params = new ArrayList<String[]>();
		for (final String param : MethodSynthesizerProxy.getAvailableSynthesizers()) {
			params.add(new String[] { param });
		}
		return params;
	}

	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testMapToCollectionMapping() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12MapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		assembler.assembleEntity(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return TestDto12CollectionIterface.class;
                } else if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("nestedEntity".equals(entityBeanKey)) {
					return new TestDto12CollectionClass();
				} else if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(eWrap.getCollectionWrapper().getItems());
		assertEquals(2, eWrap.getCollectionWrapper().getItems().size());
		Iterator<TestEntity12CollectionItemInterface> eiter;
		
		eiter = eWrap.getCollectionWrapper().getItems().iterator();
		final TestEntity12CollectionItemInterface itm1 = eiter.next();
		final TestEntity12CollectionItemInterface itm2 = eiter.next();
		assertEquals("itm2", itm1.getName());
		assertEquals("itm3", itm2.getName());
	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testMapToMapValueMapping() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12MapInterface eMap = new TestEntity12MapClass();
		eMap.setItems(new HashMap<String, TestEntity12CollectionItemInterface>());
		eMap.getItems().put("itm1", eItem1);
		eMap.getItems().put("itm2", eItem2);
		
		final TestDto12MapIterface dMap = new TestDto12MapToMapClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eMap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eMap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		assembler.assembleEntity(dMap, eMap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(eMap.getItems());
		assertEquals(2, eMap.getItems().size());
		
		final Set<String> ekeys = eMap.getItems().keySet();
		for (String key : ekeys) {
			if ("itm2".equals(key)) {
				assertEquals("itm2", eMap.getItems().get(key).getName());
			} else if ("itm3".equals(key)) {
				assertEquals("itm3", eMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}	
		
	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testMapToMapKeyMapping() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12MapByKeyInterface eMap = new TestEntity12MapByKeyClass();
		eMap.setItems(new HashMap<TestEntity12CollectionItemInterface, String>());
		eMap.getItems().put(eItem1, "itm1");
		eMap.getItems().put(eItem2, "itm2");
		
		final TestDto12MapByKeyIterface dMap = new TestDto12MapToMapByKeyClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eMap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eMap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<TestDto12CollectionItemIterface> keys = dMap.getItems().keySet();
		TestDto12CollectionItemIterface dItem1 = null;
		TestDto12CollectionItemIterface dItem2 = null;
		for (TestDto12CollectionItemIterface key : keys) {
			if ("itm1".equals(key.getName())) {
				assertEquals("itm1", dMap.getItems().get(key));
				dItem1 = key;
			} else if ("itm2".equals(key.getName())) {
				assertEquals("itm2", dMap.getItems().get(key));
				dItem2 = key;
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put(dto3, "itm3");
		
		dMap.getItems().put(dItem2, "itm no 2");
		
		dMap.getItems().remove(dItem1); // first

		assembler.assembleEntity(dMap, eMap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(eMap.getItems());
		assertEquals(2, eMap.getItems().size());
		
		final Set<TestEntity12CollectionItemInterface> ekeys = eMap.getItems().keySet();
		for (TestEntity12CollectionItemInterface key : ekeys) {
			if ("itm2".equals(key.getName())) {
				assertEquals("itm no 2", eMap.getItems().get(key));
			} else if ("itm3".equals(key.getName())) {
				assertEquals("itm3", eMap.getItems().get(key));
			} else {
				fail("Unknown key");
			}
		}	

	}
	
	/**
	 * Test collection of nested objects.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testCollectionEntityToNullProperty() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12MapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		
		dMap.setItems(null);
		
		assembler.assembleEntity(dMap, eWrap, null, null);

		assertNotNull(eWrap.getCollectionWrapper().getItems());
		assertEquals(0, eWrap.getCollectionWrapper().getItems().size());
	}

	/**
	 * Test collection of nested objects.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testMapEntityToNullProperty() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12MapInterface eMap = new TestEntity12MapClass();
		eMap.setItems(new HashMap<String, TestEntity12CollectionItemInterface>());
		eMap.getItems().put("itm1", eItem1);
		eMap.getItems().put("itm2", eItem2);
		
		final TestDto12MapIterface dMap = new TestDto12MapToMapClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eMap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eMap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		
		dMap.setItems(null);
		
		assembler.assembleEntity(dMap, eMap, null, null);

		assertNotNull(eMap.getItems());
		assertEquals(0, eMap.getItems().size());
	}
	
	/**
	 * Test collection of nested objects.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testCollectionNullToNullProperty() throws GeDAException {
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12MapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, null);
		
		assertNull(dMap.getItems());
		
		assembler.assembleEntity(dMap, eWrap, null, null);
		
		assertNull(eWrap.getCollectionWrapper().getItems());
		
	}
	
    /**
	 * Test collection of nested objects.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testMapNullToNullProperty() throws GeDAException {
		
		final TestEntity12MapInterface eMap = new TestEntity12MapClass();
		
		final TestDto12MapIterface dMap = new TestDto12MapToMapClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eMap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eMap, null, null);

		assertNull(dMap.getItems());

		assembler.assembleEntity(dMap, eMap, null, null);

		assertNull(eMap.getItems());

	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testMapToCollectionMappingNestedCreate() throws GeDAException {

		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		
		final TestDto12MapIterface dMap = new TestDto12MapToCollectionClass();
		dMap.setItems(new HashMap<String, TestDto12CollectionItemIterface>());
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
				
		final TestDto12CollectionItemClass dto1 = new TestDto12CollectionItemClass();
		dto1.setName("itm1");
		final TestDto12CollectionItemClass dto2 = new TestDto12CollectionItemClass();
		dto2.setName("itm2");
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		
		dMap.getItems().put("itm1", dto1);
		dMap.getItems().put("itm2", dto2);
		dMap.getItems().put("itm3", dto3);
		
		assembler.assembleEntity(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return TestEntity12CollectionInterface.class;
                } else if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("nestedEntity".equals(entityBeanKey)) {
					return new TestEntity12CollectionClass();
				} else if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(eWrap.getCollectionWrapper().getItems());
		assertEquals(I_3, eWrap.getCollectionWrapper().getItems().size());
		
		boolean item1 = false;
		boolean item2 = false;
		boolean item3 = false;
		for (TestEntity12CollectionItemInterface item : eWrap.getCollectionWrapper().getItems()) {
			if ("itm1".equals(item.getName())) {
				item1 = true;
			} else if ("itm2".equals(item.getName())) {
				item2 = true;
			} else if ("itm3".equals(item.getName())) {
				item3 = true;
			} else {
				fail("Unkown item");
			}
		}
		assertTrue(item1 && item2 && item3);
	}

    /**
     * Test that map pipe is able to handle immutable objects.
	 * 
	 * @throws GeDAException exception
     */
    @SuppressWarnings("unchecked")
	@Test
    public void testMapBindingOnImmutableObjects() throws GeDAException {
        final TestEntity16Class entities = new TestEntity16Class(
            (Collection) Arrays.asList(
                new TestEntity15Class("1", "one"),
                new TestEntity15Class("2", "two"),
                new TestEntity15Class("3", "three")
            )
        );

        final TestDto17Class dtos = new TestDto17Class();

        final Assembler assembler = DTOAssembler.newCustomAssembler(
                TestDto17Class.class, TestEntity16Class.class, synthesizer);

        assembler.assembleDto(dtos, entities, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("TestDto15Class".equals(entityBeanKey)) {
                    return TestDto15Class.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("TestDto15Class".equals(entityBeanKey)) {
                    return new TestDto15Class();
                }
                return null;
            }
        });

        final int noOfItems = 3;
        
        assertNotNull(dtos.getItems());
        assertEquals(noOfItems, dtos.getItems().size());
        assertEquals("1", dtos.getItems().get("1").getName());
        assertEquals("2", dtos.getItems().get("2").getName());
        assertEquals("3", dtos.getItems().get("3").getName());

        for (String dtoKey : dtos.getItems().keySet()) {
            final TestDto15Class dto = dtos.getItems().get(dtoKey);
            dto.setName("DTO_" + dtoKey);
            dtos.getItems().put(dtoKey, dto);
        }

        assembler.assembleEntity(dtos, entities, null, null);
        assertNotNull(entities.getItems());
        assertEquals(noOfItems, entities.getItems().size());
        final Iterator<TestEntity15Class> iterEntity = entities.getItems().iterator();
        assertEquals("1", iterEntity.next().getName());
        assertEquals("2", iterEntity.next().getName());
        assertEquals("3", iterEntity.next().getName());

    }
    
	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = DtoToEntityMatcherNotFoundException.class)
	public void testMapToCollectionMappingWithNoMatcherInConverters() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12aMapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		assembler.assembleEntity(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return TestDto12CollectionClass.class;
                } else if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("nestedEntity".equals(entityBeanKey)) {
					return new TestDto12CollectionClass();
				} else if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
	}

	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = NotDtoToEntityMatcherException.class)
	public void testMapToCollectionMappingWithInvalidMatcherInConverters() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12aMapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		final Map<String, Object> converters = new HashMap<String, Object>();
		converters.put("Test12KeyMapToEntityMatcher.class", new Object());
		
		assembler.assembleEntity(dMap, eWrap, converters, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return TestDto12CollectionClass.class;
                } else if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("nestedEntity".equals(entityBeanKey)) {
					return new TestDto12CollectionClass();
				} else if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testMapToCollectionMappingWithMatcherInConverters() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12aMapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		final Map<String, Object> converters = new HashMap<String, Object>();
		converters.put("Test12KeyMapToEntityMatcher.class", new Test12KeyMapToEntityMatcher());
		
		assembler.assembleEntity(dMap, eWrap, converters, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return TestDto12CollectionClass.class;
                } else if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("nestedEntity".equals(entityBeanKey)) {
					return new TestDto12CollectionClass();
				} else if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(eWrap.getCollectionWrapper().getItems());
		assertEquals(2, eWrap.getCollectionWrapper().getItems().size());
		Iterator<TestEntity12CollectionItemInterface> eiter;
		
		eiter = eWrap.getCollectionWrapper().getItems().iterator();
		final TestEntity12CollectionItemInterface itm1 = eiter.next();
		final TestEntity12CollectionItemInterface itm2 = eiter.next();
		assertEquals("itm2", itm1.getName());
		assertEquals("itm3", itm2.getName());
	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = BeanFactoryNotFoundException.class)
	public void testMapToCollectionMappingWithDtoMapKeyNoBeanFactory() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12bMapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, null);
		
	}

	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = UnableToCreateInstanceException.class)
	public void testMapToCollectionMappingWithDtoMapKeyBeanFactoryUnableToInstantiate() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12bMapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				}
				return null;
			}
			
		});
	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testMapToCollectionMappingWithDtoMapKey() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12bMapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				} else if ("dtoMap".equals(entityBeanKey)) {
					return new HashMap<Object, Object>();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		final Map<String, Object> converters = new HashMap<String, Object>();
		converters.put("Test12KeyMapToEntityMatcher.class", new Test12KeyMapToEntityMatcher());
		
		assembler.assembleEntity(dMap, eWrap, converters, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return TestDto12CollectionClass.class;
                } else if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("nestedEntity".equals(entityBeanKey)) {
					return new TestDto12CollectionClass();
				} else if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
		
		assertNotNull(eWrap.getCollectionWrapper().getItems());
		assertEquals(2, eWrap.getCollectionWrapper().getItems().size());
		Iterator<TestEntity12CollectionItemInterface> eiter;
		
		eiter = eWrap.getCollectionWrapper().getItems().iterator();
		final TestEntity12CollectionItemInterface itm1 = eiter.next();
		final TestEntity12CollectionItemInterface itm2 = eiter.next();
		assertEquals("itm2", itm1.getName());
		assertEquals("itm3", itm2.getName());
	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = BeanFactoryNotFoundException.class)
	public void testMapToCollectionMappingWithEntityMapKeyNoBeanFactory() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12bMapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				} else if ("dtoMap".equals(entityBeanKey)) {
					return new HashMap<Object, Object>();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		final Map<String, Object> converters = new HashMap<String, Object>();
		converters.put("Test12KeyMapToEntityMatcher.class", new Test12KeyMapToEntityMatcher());
		
		eWrap.getCollectionWrapper().setItems(null);
		
		assembler.assembleEntity(dMap, eWrap, converters, null);

	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test(expected = UnableToCreateInstanceException.class)
	public void testMapToCollectionMappingWithEntityMapKeyBeanFactoryUnableToCreateInstance() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12bMapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				} else if ("dtoMap".equals(entityBeanKey)) {
					return new HashMap<Object, Object>();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		final Map<String, Object> converters = new HashMap<String, Object>();
		converters.put("Test12KeyMapToEntityMatcher.class", new Test12KeyMapToEntityMatcher());
		
		eWrap.getCollectionWrapper().setItems(null);
		
		assembler.assembleEntity(dMap, eWrap, converters, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return TestDto12CollectionClass.class;
                } else if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("nestedEntity".equals(entityBeanKey)) {
					return new TestDto12CollectionClass();
				} else if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				}
				return null;
			}
			
		});
	}
	
	/**
	 * Test that DTO map correctly maps to entity collection.
	 * 
	 * @throws GeDAException exception
	 */
	@Test
	public void testMapToCollectionMappingWithEntityMapKey() throws GeDAException {
		final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
		eItem1.setName("itm1");
		final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
		eItem2.setName("itm2");
		
		final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
		eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
		eColl.getItems().add(eItem1);
		eColl.getItems().add(eItem2);
		
		final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
		eWrap.setCollectionWrapper(eColl);
		
		final TestDto12MapIterface dMap = new TestDto12bMapToCollectionClass();
		
		final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);
		
		assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("dtoItem".equals(entityBeanKey)) {
					return new TestDto12CollectionItemClass();
				} else if ("dtoMap".equals(entityBeanKey)) {
					return new HashMap<Object, Object>();
				}
				return null;
			}
			
		});
		
		assertNotNull(dMap.getItems());
		assertEquals(2, dMap.getItems().size());
		final Set<String> keys = dMap.getItems().keySet();
		for (String key : keys) {
			if ("itm1".equals(key)) {
				assertEquals("itm1", dMap.getItems().get(key).getName());
			} else if ("itm2".equals(key)) {
				assertEquals("itm2", dMap.getItems().get(key).getName());
			} else {
				fail("Unknown key");
			}
		}
		
		final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
		dto3.setName("itm3");
		dMap.getItems().put("itm3", dto3);
		
		dMap.getItems().remove("itm1"); // first
		
		final Map<String, Object> converters = new HashMap<String, Object>();
		converters.put("Test12KeyMapToEntityMatcher.class", new Test12KeyMapToEntityMatcher());
		
		eWrap.getCollectionWrapper().setItems(null);
		
		assembler.assembleEntity(dMap, eWrap, converters, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return TestDto12CollectionClass.class;
                } else if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
				if ("nestedEntity".equals(entityBeanKey)) {
					return new TestDto12CollectionClass();
				} else if ("entityItem".equals(entityBeanKey)) {
					return new TestEntity12CollectionItemClass();
				} else if ("entityMapOrCollection".equals(entityBeanKey)) {
					return new ArrayList<Object>();
				}
				return null;
			}
			
		});
		
		assertNotNull(eWrap.getCollectionWrapper().getItems());
		assertEquals(2, eWrap.getCollectionWrapper().getItems().size());
		Iterator<TestEntity12CollectionItemInterface> eiter;
		
		eiter = eWrap.getCollectionWrapper().getItems().iterator();
		while (eiter.hasNext()) {
			TestEntity12CollectionItemInterface itm = (TestEntity12CollectionItemInterface) eiter.next();
			if (!"itm2".equals(itm.getName()) && !"itm3".equals(itm.getName())) {
				fail("Invalid element: " + itm.getName());
			}
		}
	}


    /**
     * Test that DTO map correctly maps to entity collection.
     *
     * @throws GeDAException exception
     */
    @Test
    public void testMapToCollectionMappingWithDtoMapKeyWithGenericTypeIoC() throws GeDAException {
        final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
        eItem1.setName("itm1");
        final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
        eItem2.setName("itm2");

        final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
        eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
        eColl.getItems().add(eItem1);
        eColl.getItems().add(eItem2);

        final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
        eWrap.setCollectionWrapper(eColl);

        final TestDto12MapIterface dMap = new TestDto12cMapToCollectionClass();

        final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);

        assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                } else if ("TestEntity12CollectionItemInterface".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return new TestDto12CollectionItemClass();
                } else if ("dtoMap".equals(entityBeanKey)) {
                    return new HashMap<Object, Object>();
                }
                return null;
            }

        });

        assertNotNull(dMap.getItems());
        assertEquals(2, dMap.getItems().size());
        final Set<String> keys = dMap.getItems().keySet();
        for (String key : keys) {
            if ("itm1".equals(key)) {
                assertEquals("itm1", dMap.getItems().get(key).getName());
            } else if ("itm2".equals(key)) {
                assertEquals("itm2", dMap.getItems().get(key).getName());
            } else {
                fail("Unknown key");
            }
        }

        final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
        dto3.setName("itm3");
        dMap.getItems().put("itm3", dto3);

        dMap.getItems().remove("itm1"); // first

        final Map<String, Object> converters = new HashMap<String, Object>();
        converters.put("Test12KeyMapToEntityMatcher.class", new Test12KeyMapToEntityMatcher());

        assembler.assembleEntity(dMap, eWrap, converters, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return TestDto12CollectionClass.class;
                } else if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                } else if ("TestEntity12CollectionItemInterface".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return new TestDto12CollectionClass();
                } else if ("entityItem".equals(entityBeanKey)) {
                    return new TestEntity12CollectionItemClass();
                }
                return null;
            }

        });

        assertNotNull(eWrap.getCollectionWrapper().getItems());
        assertEquals(2, eWrap.getCollectionWrapper().getItems().size());
        Iterator<TestEntity12CollectionItemInterface> eiter;

        eiter = eWrap.getCollectionWrapper().getItems().iterator();
        final TestEntity12CollectionItemInterface itm1 = eiter.next();
        final TestEntity12CollectionItemInterface itm2 = eiter.next();
        assertEquals("itm2", itm1.getName());
        assertEquals("itm3", itm2.getName());
    }

    /**
     * Test that DTO map correctly maps to entity collection.
     *
     * @throws GeDAException exception
     */
    @Test
    public void testMapToCollectionMappingWithDtoMapKeyWithGenericTypeAuto() throws GeDAException {
        final TestEntity12CollectionItemInterface eItem1 = new TestEntity12CollectionItemClass();
        eItem1.setName("itm1");
        final TestEntity12CollectionItemInterface eItem2 = new TestEntity12CollectionItemClass();
        eItem2.setName("itm2");

        final TestEntity12CollectionInterface eColl = new TestEntity12CollectionClass();
        eColl.setItems(new ArrayList<TestEntity12CollectionItemInterface>());
        eColl.getItems().add(eItem1);
        eColl.getItems().add(eItem2);

        final TestEntity12WrapCollectionInterface eWrap = new TestEntity12WrapCollectionClass();
        eWrap.setCollectionWrapper(eColl);

        final TestDto12MapIterface dMap = new TestDto12dMapToCollectionClass();

        final Assembler assembler = DTOAssembler.newCustomAssembler(dMap.getClass(), eWrap.getClass(), synthesizer);

        assembler.assembleDto(dMap, eWrap, null, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return TestDto12CollectionItemClass.class;
                } else if ("TestEntity12CollectionItemInterface".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("dtoItem".equals(entityBeanKey)) {
                    return new TestDto12CollectionItemClass();
                } else if ("dtoMap".equals(entityBeanKey)) {
                    return new HashMap<Object, Object>();
                }
                return null;
            }

        });

        assertNotNull(dMap.getItems());
        assertEquals(2, dMap.getItems().size());
        final Set<String> keys = dMap.getItems().keySet();
        for (String key : keys) {
            if ("itm1".equals(key)) {
                assertEquals("itm1", dMap.getItems().get(key).getName());
            } else if ("itm2".equals(key)) {
                assertEquals("itm2", dMap.getItems().get(key).getName());
            } else {
                fail("Unknown key");
            }
        }

        final TestDto12CollectionItemClass dto3 = new TestDto12CollectionItemClass();
        dto3.setName("itm3");
        dMap.getItems().put("itm3", dto3);

        dMap.getItems().remove("itm1"); // first

        final Map<String, Object> converters = new HashMap<String, Object>();
        converters.put("Test12KeyMapToEntityMatcher.class", new Test12KeyMapToEntityMatcher());

        assembler.assembleEntity(dMap, eWrap, converters, new BeanFactory() {

            public Class getClazz(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return TestDto12CollectionClass.class;
                } else if ("entityItem".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                } else if ("TestEntity12CollectionItemInterface".equals(entityBeanKey)) {
                    return TestEntity12CollectionItemInterface.class;
                }
                return null;
            }

            public Object get(final String entityBeanKey) {
                if ("nestedEntity".equals(entityBeanKey)) {
                    return new TestDto12CollectionClass();
                } else if ("entityItem".equals(entityBeanKey)) {
                    return new TestEntity12CollectionItemClass();
                }
                return null;
            }

        });

        assertNotNull(eWrap.getCollectionWrapper().getItems());
        assertEquals(2, eWrap.getCollectionWrapper().getItems().size());
        Iterator<TestEntity12CollectionItemInterface> eiter;

        eiter = eWrap.getCollectionWrapper().getItems().iterator();
        final TestEntity12CollectionItemInterface itm1 = eiter.next();
        final TestEntity12CollectionItemInterface itm2 = eiter.next();
        assertEquals("itm2", itm1.getName());
        assertEquals("itm3", itm2.getName());
    }

}
