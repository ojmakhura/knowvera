import { Logger, LogLevel, LogOutput } from './logger.service';

const logMethods: Array<'log' | 'info' | 'warn' | 'error'> = ['log', 'info', 'warn', 'error'];

type LogOutputSpy = LogOutput & {
  calls: any[][];
};

const createLogOutputSpy = (): LogOutputSpy => {
  const spy = ((...args: any[]) => {
    spy.calls.push(args);
  }) as LogOutputSpy;

  spy.calls = [];
  return spy;
};

describe('Logger', () => {
  let savedConsole: Partial<Record<'log' | 'info' | 'warn' | 'error', (...args: any[]) => void>>;
  let savedLevel: LogLevel;
  let savedOutputs: LogOutput[];

  beforeAll(() => {
    savedConsole = {};
    logMethods.forEach((m) => {
      savedConsole[m] = console[m];
      console[m] = () => {};
    });
    savedLevel = Logger.level;
    savedOutputs = Logger.outputs;
  });

  beforeEach(() => {
    Logger.level = LogLevel.Debug;
  });

  afterAll(() => {
    logMethods.forEach((m) => {
      const original = savedConsole[m];
      if (original) {
        console[m] = original;
      }
    });
    Logger.level = savedLevel;
    Logger.outputs = savedOutputs;
  });

  it('should create an instance', () => {
    expect(new Logger()).toBeTruthy();
  });

  it('should add a new LogOutput and receives log entries', () => {
    // Arrange
    const outputSpy = createLogOutputSpy();
    const log = new Logger('test');

    // Act
    Logger.outputs.push(outputSpy);

    log.debug('d');
    log.info('i');
    log.warn('w');
    log.error('e', { error: true });

    // Assert
    expect(outputSpy.calls.length).toBe(4);
    expect(outputSpy.calls).toContainEqual(['test', LogLevel.Debug, 'd']);
    expect(outputSpy.calls).toContainEqual(['test', LogLevel.Info, 'i']);
    expect(outputSpy.calls).toContainEqual(['test', LogLevel.Warning, 'w']);
    expect(outputSpy.calls).toContainEqual(['test', LogLevel.Error, 'e', { error: true }]);
  });

  it('should add a new LogOutput and receives only production log entries', () => {
    // Arrange
    const outputSpy = createLogOutputSpy();
    const log = new Logger('test');

    // Act
    Logger.outputs.push(outputSpy);
    Logger.enableProductionMode();

    log.debug('d');
    log.info('i');
    log.warn('w');
    log.error('e', { error: true });

    // Assert
    expect(outputSpy.calls.length).toBe(2);
    expect(outputSpy.calls).toContainEqual(['test', LogLevel.Warning, 'w']);
    expect(outputSpy.calls).toContainEqual(['test', LogLevel.Error, 'e', { error: true }]);
  });
});
